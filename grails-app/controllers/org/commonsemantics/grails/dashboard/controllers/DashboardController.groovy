/*
 * Copyright 2014 Massachusetts General Hospital
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.commonsemantics.grails.dashboard.controllers

import org.commonsemantics.grails.agents.commands.PersonCreateCommand
import org.commonsemantics.grails.agents.model.Person
import org.commonsemantics.grails.groups.model.Group
import org.commonsemantics.grails.groups.model.UserGroup
import org.commonsemantics.grails.systems.model.SystemApi
import org.commonsemantics.grails.users.commands.UserCreateCommand
import org.commonsemantics.grails.users.model.ProfilePrivacy
import org.commonsemantics.grails.users.model.Role
import org.commonsemantics.grails.users.model.User
import org.commonsemantics.grails.users.model.UserProfilePrivacy
import org.commonsemantics.grails.users.model.UserRole
import org.commonsemantics.grails.users.utils.DefaultUsersProfilePrivacy
import org.commonsemantics.grails.users.utils.DefaultUsersRoles
import org.commonsemantics.grails.users.utils.UserStatus
import org.commonsemantics.grails.users.utils.UsersUtils

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
class DashboardController {
	
	def springSecurityService
	def agentsService
	def usersService
	def groupsService
	def systemsService
	
	/*
	 * Loading by primary key is usually more efficient because it takes
	 * advantage of Hibernate's first-level and second-level caches
	 */
	protected def injectUserProfile() {
		def principal = springSecurityService.principal
		if(principal.equals("anonymousUser")) {
			redirect(controller: "login", action: "index");
		} else {
			String userId = principal.id
			def user = User.findById(userId);
			if(user==null) {
				log.error "Error:User not found for id: " + userId
				render (view:'error', model:[message: "User not found for id: "+userId]);
			}
			user
		}
	}
	
	// ------------------------------------------------------------------------
	//  CS-USERS:User
	// ------------------------------------------------------------------------
	
	def listUsersRoles = {
		def user = injectUserProfile()

		if (!params.max) params.max = 15
		if (!params.offset) params.offset = 0
		if (!params.sort) params.sort = "authority"
		if (!params.order) params.order = "asc"

		def results = usersService.listRoles(params.max, params.offset, params.sort, params.order);

		render (view:'users-roles', model:[user : user, "roles" : results[0], "rolesTotal": Role.count(), "rolesCount": results[1], "menuitem" : "listRoles"])
	}
	
	def listUsers = {
		log.debug("List-users max:" + params.max + " offset:" + params.offset)
		render (view:'users-list', model:[users:User.list(params), usersTotal: User.count(), max: params.max, offset: params.offset, "menuitem" : "listUsers"]);
	}
	
	def showUser = {
		def user = User.findById(params.id);
		render (view:'user-show', model:[label:params.testId, description:params.testDescription, user:user]);
	}
	
	def createUser = {
		def user = injectUserProfile()
		render (view:'user-create',  model:[action: "create", roles: Role.list(), defaultRole: DefaultUsersRoles.USER, "menuitem" : "createUser"]);
	}
	
	def searchUser = {
		render (view:'users-search', model:["menuitem" : "searchUser"]);
	}
	
	def saveUser = {PersonCreateCommand cmd ->
		log.debug("[TEST] save-user " + cmd.displayName);
		UserCreateCommand c = new UserCreateCommand();
		def validationFailed = agentsService.validatePerson(cmd);
		if (validationFailed) {
			log.error("[TEST] While Saving User's Person " + cmd.errors)
		} else {
			def person = new Person();
			person.title = params.title;
			person.firstName = params.firstName;
			person.middleName = params.middleName;
			person.lastName = params.lastName;
			person.affiliation = params.affiliation;
			person.country = params.country;
			person.displayName = params.displayName;
			person.email = params.email;

			Person.withTransaction { personStatus ->
				if(!person.save(flush: true)) {
					log.error("[TEST] While Saving User's Person " + person.errors)
					person.errors.each {
						// http://grails.org/doc/latest/api/grails/validation/ValidationErrors.html
						log.error("[TEST] While Saving User's Person " + it.target)
						it.fieldErrors.each { error ->
							// http://docs.spring.io/spring/docs/1.2.9/api/org/springframework/validation/FieldError.html
							// println '---- error ----' + error.getClass().getName()
							// println '---- error ----' + error.getField()
							// println '---- error ----' + error.getDefaultMessage()
							cmd.errors.rejectValue(error.getField(),
									g.message(code: 'org.commonsemantics.grails.users.model.field.username.not.available.message', default: error.getDefaultMessage()))
						}
					}
				} else {
					def user = new User(username: params.username, person:person)
					
					if(c.isPasswordValid()) {
						user.password = params.password;
					} else {
						log.error("Passwords not matching while saving " + it.target)
						c.errors.rejectValue("password",
							g.message(code: 'org.commonsemantics.grails.users.model.field.password.not.matching.message', default: "Passwords not matching"));
					}
					
					if(!user.save(flush: true)) {
						log.error("[TEST] While Saving User " + cmd.errors)
						user.errors.each {
							// http://grails.org/doc/latest/api/grails/validation/ValidationErrors.html
							log.error("[TEST] While Saving User " + it.target)
							it.fieldErrors.each { error ->
								// http://docs.spring.io/spring/docs/1.2.9/api/org/springframework/validation/FieldError.html
								//println '---- error ----' + error.getClass().getName()
								//println '---- error ----' + error.getField()
								//println '---- error ----' + error.getDefaultMessage()
								c.errors.rejectValue(error.getField(),
										g.message(code: 'org.commonsemantics.grails.users.model.field.username.not.available.message', default: error.getDefaultMessage()))

								println c.errors
							}
						}
						log.error("[TEST] Rolling back User's Person " + person)
						personStatus.setRollbackOnly();

						c.username = params.username;
						
						if(c.isPasswordValid()) {
							c.password = params.password;
						} else {
							log.error("Passwords not matching while saving " + it.target)
							c.errors.rejectValue("password",
								g.message(code: 'org.commonsemantics.grails.users.model.field.password.not.matching.message', default: "Passwords not matching"));
						}
						
						c.status = params.userStatus;
						c.person = cmd;
						usersService.validateUser(c);

						def usersRoles = [];
						if(params.Administrator=='on') {
							usersRoles.add(Role.findByAuthority(DefaultUsersRoles.ADMIN.value()));
						}
						if(params.Manager=='on') {
							usersRoles.add(Role.findByAuthority(DefaultUsersRoles.MANAGER.value()));
						}
						if(params.User=='on') {
							usersRoles.add(Role.findByAuthority(DefaultUsersRoles.USER.value()));
						}
						render (view:'user-create', model:[label:params.testId, description:params.testDescription, user:c, userRoles: usersRoles]);
					} else {
						log.debug("[TEST] save-user roles, privacy and status");
						usersService.updateUserRole(user, Role.findByAuthority(DefaultUsersRoles.ADMIN.value()), params.Administrator)
						usersService.updateUserRole(user, Role.findByAuthority(DefaultUsersRoles.MANAGER.value()), params.Manager)
						usersService.updateUserRole(user, Role.findByAuthority(DefaultUsersRoles.USER.value()), params.User)

						usersService.updateUserProfilePrivacy(user, params.userProfilePrivacy)				
						usersService.updateUserStatus(user, params.userStatus)

						render (view:'user-show', model:[label:params.testId, description:params.testDescription, user:user]);
						return;
					}
				}
			}
		}
	}
	
	
	
	
	
	// ------------------------------------------------------------------------
	//  CS-GROUPS:Group
	// ------------------------------------------------------------------------
	
	def listGroups = {
		if (!params.max) params.max = 15
		if (!params.offset) params.offset = 0
		if (!params.sort) params.sort = "groupsCount"
		if (!params.order) params.order = "asc"

		def results = groupsService.listGroups(params.max, params.offset, params.sort, params.order);

		def groupsUsersCount = [:]
		def groups = results[0];
		
		groups.each { group ->
			groupsUsersCount.put (group.id, UserGroup.findAllWhere(group: group).size())
		}
		
		render (view:'listGroups', model:["groups" : results[0], "groupsTotal": Group.count(), "groupsCount": results[1], groupsUsersCount: groupsUsersCount, "menuitem" : "listGroups",
			appBaseUrl: request.getContextPath()])
	}
	
	def createGroup = {
		render (view:'createGroup',  model:[action: "create", "menuitem" : "createGroup"]);
	}
	
	def listSystems = {
		if (!params.max) params.max = 15
		if (!params.offset) params.offset = 0
		if (!params.sort) params.sort = "systemsCount"
		if (!params.order) params.order = "asc"

		def results = systemsService.listSystems(params.max, params.offset, params.sort, params.order);

		render (view:'listSystems', model:["systems" : results[0], "systemsTotal": SystemApi.count(), "systemsCount": results[1], "menuitem" : "listSystems",
			appBaseUrl: request.getContextPath()])
	}
}
