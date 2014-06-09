/*
 * Copyright 2014  Common Semantics (commonsemantics.org)
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

import grails.converters.JSON

import org.codehaus.groovy.grails.plugins.web.taglib.ValidationTagLib
import org.commonsemantics.grails.agents.commands.PersonCreateCommand
import org.commonsemantics.grails.agents.commands.PersonEditCommand
import org.commonsemantics.grails.agents.model.Person
import org.commonsemantics.grails.groups.commands.GroupCreateCommand
import org.commonsemantics.grails.groups.commands.GroupEditCommand
import org.commonsemantics.grails.groups.model.Group
import org.commonsemantics.grails.groups.model.GroupRole
import org.commonsemantics.grails.groups.model.UserGroup
import org.commonsemantics.grails.groups.model.UserStatusInGroup
import org.commonsemantics.grails.groups.utils.DefaultGroupRoles
import org.commonsemantics.grails.groups.utils.DefaultUserStatusInGroup
import org.commonsemantics.grails.systems.commands.SystemApiCreateCommand
import org.commonsemantics.grails.systems.commands.SystemApiEditCommand
import org.commonsemantics.grails.systems.model.SystemApi
import org.commonsemantics.grails.systems.model.UserSystemApi
import org.commonsemantics.grails.users.commands.UserCreateCommand
import org.commonsemantics.grails.users.commands.UserResetPasswordCommand
import org.commonsemantics.grails.users.model.Role
import org.commonsemantics.grails.users.model.User
import org.commonsemantics.grails.users.model.UserRole
import org.commonsemantics.grails.users.utils.DefaultUsersRoles
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
	
	def index = {
		def user = injectUserProfile();
		render (view:'index', model:[user : user]);
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
	
	def editUser = {
		def user = User.findById(params.id);
		render (view:'user-edit', model:[user:user, userRoles: UsersUtils.getUserRoles(user)]);
	}
	
	def createUser = {
		def user = injectUserProfile()
		render (view:'user-create',  model:[action: "create", roles: Role.list(), defaultRole: DefaultUsersRoles.USER, "menuitem" : "createUser"]);
	}
	
	def searchUser = {
		render (view:'users-search', model:["menuitem" : "searchUser"]);
	}
	
	def lockUser = {
		def user = User.findById(params.id)
		user.accountLocked = true
		user.enabled = true;
		if(params.redirect)
			redirect(action:params.redirect, params:[id: params.id])
		else
			redirect(action:'showUser', params:[id: params.id])
	}

	def unlockUser = {
		def user = User.findById(params.id)
		user.accountLocked = false
		user.enabled = true;
		if(params.redirect)
			redirect(action:params.redirect, params:[id: params.id])
		else
			redirect(action:'showUser', params:[id: params.id])
	}

	def enableUser = {
		def user = User.findById(params.id)
		user.enabled = true
		user.accountLocked = false
		if(params.redirect)
			redirect(action:params.redirect, params:[id: params.id])
		else
			redirect(action:'showUser', params:[id: params.id])
	}

	def disableUser = {
		def user = User.findById(params.id)
		user.enabled = false
		user.accountLocked = false
		if(params.redirect)
			redirect(action:params.redirect, params:[id: params.id])
		else
			redirect(action:'showUser', params:[id: params.id])
	}
	
	def changeUserPassword = {
		def loggedUser = injectUserProfile()
		def user = User.findById(params.id)
		
		render (view:'user-password', model: [
			loggedUser:loggedUser, user: user]);
	}
	
	def saveUserPassword = {UserResetPasswordCommand userResetPasswordCommand->
		def loggedUser = injectUserProfile()
		def user = User.findById(params.user)
		if(userResetPasswordCommand.hasErrors()) {
			userResetPasswordCommand.errors.allErrors.each { println it }
			render(view:'user-password', model:[
				loggedUser:loggedUser, 
				user: user, item:userResetPasswordCommand,
				msgError: 'The password has not been saved successfully'])
		} else {
			render user.password
			user.password = springSecurityService.encodePassword(userResetPasswordCommand.password);
			
			redirect(action:'showUser', params:[id: params.user, msgSuccess: 'Password saved successfully']);
		}
	}
	
	def performSearchUser = {
		def user = injectUserProfile()

		if (!params.max) params.max = 1
		if (!params.offset) params.offset = 0
		if (!params.sort) params.sort = "name"
		if (!params.order) params.order = "asc"

		//TODO fix pagination
		def users = [];
		if (params.sort == 'status') {
			def buffer = [];
			def usersStatus = [:]
			User.list().each { auser ->
				usersStatus.put (auser.id, auser.status)
			}
			usersStatus = usersStatus.sort{ a, b -> a.value.compareTo(b.value) }
			if(params.order == "desc")
				usersStatus.each { userStatus ->
					buffer.add(User.findById(userStatus.key));
				}
			else
				usersStatus.reverseEach { userStatus ->
					buffer.add(User.findById(userStatus.key));
				}

			int offset = (params.offset instanceof String) ? Integer.parseInt(params.offset) : params.offset
			int max = (params.max instanceof String) ? Integer.parseInt(params.max) : params.max
			for(int i=offset;i< Math.min(offset+max+1, usersStatus.size()); i++) {
				users.add(buffer[i]);
			}
		} else if (params.sort == 'isAdmin' || params.sort == 'isAnalyst' || params.sort == 'isManager'
			|| params.sort == 'isCurator' || params.sort == 'isUser') {

		} else if (params.sort == 'name') {
			def buffer = [];
			def usersNames = [:]
			User.list().each { auser ->
				usersNames.put (auser.id, auser.person.displayName)
			}

			usersNames = usersNames.sort{ a, b -> a.value.compareTo(b.value) }
			if(params.order == "desc")
				usersNames.each { userName ->
					buffer.add(User.findById(userName.key));
				}
			else
				usersNames.reverseEach { userName ->
					buffer.add(User.findById(userName.key));
				}
				
			int offset = (params.offset instanceof String) ? Integer.parseInt(params.offset) : params.offset
			int max = (params.max instanceof String) ? Integer.parseInt(params.max) : params.max
			for(int i=offset;i< Math.min(offset+max+1, usersNames.size()); i++) {
				users.add(buffer[i]);
			}
		} else {
			// Search with no ordering
			def personCriteria = Person.createCriteria();
			def userStatusCriteria = User.createCriteria();
			def r = [];
			if(params.firstName!=null && params.firstName.trim().length()>0 &&
			params.lastName!=null && params.lastName.trim().length()>0) {
				r = personCriteria.list {
					maxResults(params.max?.toInteger())
					firstResult(params.offset?.toInteger())
					order(params.sort, params.order)
					and {
						like('firstName', params.firstName)
						like('lastName', params.lastName)
					}
				}
				r.toList().each{ person ->
					def u = User.findByPerson(person);
					users <- u;
				}
			} else if(params.firstName!=null && params.firstName.trim().length()>0 &&
			(params.lastName==null || params.lastName.trim().length()==0)) {
				r = personCriteria.list {
					maxResults(params.max?.toInteger())
					firstResult(params.offset?.toInteger())
					order(params.sort, params.order)
					like('firstName', params.firstName)
				}
				r.toList().each{ person ->
					def u = User.findByPerson(person);
					users <- u;
				}
			} else if((params.firstName==null || params.firstName.trim().length()==0) &&
			params.lastName!=null && params.lastName.trim().length()>0) {
				r = personCriteria.list {
					maxResults(params.max?.toInteger())
					firstResult(params.offset?.toInteger())
					order(params.sort, params.order)
					like('lastName', params.lastName)
				}
				r.toList().each{ person ->
					def u = User.findByPerson(person);
					users <- u;
				}
			} else if(params.displayName!=null && params.displayName.trim().length()>0) {
				r = personCriteria.list {
					maxResults(params.max?.toInteger())
					firstResult(params.offset?.toInteger())
					order(params.sort, params.order)
					like('displayName', params.displayName)
				}
				r.toList().each{ person ->
					def u = User.findByPerson(person);
					users <- u;
				}
			} else {
				r = User.list(max: params.max, offset: params.offset)
			}
		} 

		def usersResults = []
		users.each { userItem ->
			def roles = UserRole.findAllByUser(userItem);
			def userResult = [id:userItem.id, username:userItem.username, name: userItem.person.firstName + " " + userItem.person.lastName,
						displayName: userItem.person.getDisplayName(),
						isAdmin: roles.role.authority.contains(DefaultUsersRoles.ADMIN.value()), isManager: roles.role.authority.contains(DefaultUsersRoles.MANAGER.value()),
						isUser: roles.role.authority.contains(DefaultUsersRoles.USER.value()), email: userItem.person.getEmail(),
						status: UsersUtils.getStatusLabel(userItem), dateCreated: userItem.dateCreated]
			usersResults << userResult
		}

		def paginationResults = ['offset':params.offset+params.max, 'sort':params.sort, 'order':params.order]


		def results = [users: usersResults, pagination: paginationResults]
		render results as JSON
	}
	
	def saveUser = {PersonCreateCommand cmd ->
		log.debug("save-user " + cmd.displayName);
		def g = new ValidationTagLib()
		UserCreateCommand c = new UserCreateCommand();
		def validationFailed = agentsService.validatePerson(cmd);
		if (validationFailed) {
			log.error("While Saving User's Person " + cmd.errors)
			cmd.errors.allErrors.each { println "----> " + it }
			c.username = params.username;
			c.userStatus = params.userStatus;
			c.person = cmd;
			render (view:'user-create', model:[user:c]);
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
					println UsersUtils.getProfilePrivacy(params.userProfilePrivacy);
					def user = new User(username: params.username, person:person, password: encodePassword(params.password), profilePrivacy: UsersUtils.getProfilePrivacy(params.userProfilePrivacy))
					if(!user.save(flush: true)) {
						log.error("[TEST] While Saving User " + cmd.errors)
						user.errors.each {
							// http://grails.org/doc/latest/api/grails/validation/ValidationErrors.html
							log.error("[TEST] While Saving User " + it.target)
							
							it.fieldErrors.each { error ->
								// http://docs.spring.io/spring/docs/1.2.9/api/org/springframework/validation/FieldError.html
								println '---- error ----' + error.getClass().getName()
								println '---- error ----' + error.getField()
								println '---- error ----' + error.getDefaultMessage()
								c.errors.rejectValue(error.getField(),
										g.message(code: 'org.commonsemantics.grails.users.model.field.username.not.available.message', default: error.getDefaultMessage()))

								println c.errors
							}
						}
						log.error("[TEST] Rolling back User's Person " + person)
						personStatus.setRollbackOnly();

						c.username = params.username;
						c.password = params.password;
						c.passwordConfirmation = params.passwordConfirmation;

						// Just for validation purposes
						c.userStatus = params.userStatus;						
						c.userProfilePrivacy = params.userProfilePrivacy
						c.Administrator = params.Administrator
						c.Manager = params.Manager
						c.User = params.User
						
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
						render (view:'user-create', model:[user:c, userRoles: usersRoles]);
					} else {
						log.debug("[TEST] save-user roles, privacy and status");
						
						if(c.isPasswordValid()) {
							user.password = encodePassword(params.password);
						} else {
							log.error("x3 - Passwords not matching while saving " + it.target)
							c.errors.rejectValue("password",
								g.message(code: 'org.commonsemantics.grails.users.model.field.password.not.matching.message', default: "Passwords not matching"));
						
						
							c.username = params.username;
							c.password = params.password;
							c.passwordConfirmation = params.passwordConfirmation;
							
							// Just for validation purposes
							c.userStatus = params.userStatus;
							c.userProfilePrivacy = params.userProfilePrivacy
							c.Administrator = params.Administrator
							c.Manager = params.Manager
							c.User = params.User
							
							c.person = cmd;
							render (view:'user-create', model:[user:c]);
							return;
						}
						
						def selectedRole = false;
						selectedRole = selectedRole || usersService.updateUserRole(user, Role.findByAuthority(DefaultUsersRoles.ADMIN.value()), params.Administrator)
						selectedRole = selectedRole || usersService.updateUserRole(user, Role.findByAuthority(DefaultUsersRoles.MANAGER.value()), params.Manager)
						selectedRole = selectedRole || usersService.updateUserRole(user, Role.findByAuthority(DefaultUsersRoles.USER.value()), params.User)
						if(!selectedRole) usersService.updateUserRole(user, Role.findByAuthority(DefaultUsersRoles.USER.value()), "on")
						
						//usersService.updateUserProfilePrivacy(user, params.userProfilePrivacy)				
						usersService.updateUserStatus(user, params.userStatus)

						render (view:'user-show', model:[user:user]);
						return;
					}
				}
			}
		}
	}
	
	def encodePassword(def password) {
		return springSecurityService.encodePassword(password)
	}
	
	def updateUser = { PersonEditCommand personEditCmd ->
		def validationFailed = agentsService.validatePerson(personEditCmd);
		if (validationFailed) {
			log.error("While Saving User's Person " + personEditCmd.errors)
			
			
			
			render(view:'user-edit', model:[item:userEditCmd])
		} else {
			def user = User.findById(params.id)
			
			usersService.updateUserRole(user, Role.findByAuthority(DefaultUsersRoles.ADMIN.value()), params.Administrator)
			usersService.updateUserRole(user, Role.findByAuthority(DefaultUsersRoles.MANAGER.value()), params.Manager)
			usersService.updateUserRole(user, Role.findByAuthority(DefaultUsersRoles.USER.value()), params.User)

			println '+++++ ' + params.userProfilePrivacy
			
			usersService.updateUserProfilePrivacy(user, params.userProfilePrivacy)
			usersService.updateUserStatus(user, params.status)
			
			def person = user.person;
			person.title = params.title;
			person.firstName = params.firstName;
			person.middleName = params.middleName;
			person.lastName = params.lastName;
			person.affiliation = params.affiliation;
			person.country = params.country;
			person.displayName = params.displayName;
			person.email = params.email;
			
			render (view:'user-show', model:[user: user, 
				appBaseUrl: request.getContextPath()])
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
		
		render (view:'groups-list', model:["groups" : results[0], "groupsTotal": Group.count(), "groupsCount": results[1], groupsUsersCount: groupsUsersCount, "menuitem" : "listGroups",
			appBaseUrl: request.getContextPath()])
	}
	
	def lockGroup = {
		def group = Group.findById(params.id)
		log.debug 'Locking Group ' + group
		group.locked = true
		group.enabled = true;
		if(params.redirect)
			redirect(action:params.redirect, params:[])
		else
			render (view:'group-show', model:[item: group])
	}

	def unlockGroup = {
		def group = Group.findById(params.id)
		log.debug 'Unlocking Group ' + group
		group.locked = false
		group.enabled = true;
		if(params.redirect)
			redirect(action:params.redirect, params:[])
		else
			render (view:'group-show', model:[item: group])
	}

	def enableGroup = {
		def group = Group.findById(params.id)
		log.debug 'Enabling Group ' + group
		group.enabled = true
		group.locked = false
		if(params.redirect)
			redirect(action:params.redirect, params:[])
		else
			render (view:'group-show', model:[item: group])
	}

	def disableGroup = {
		def group = Group.findById(params.id)
		log.debug 'Disabling Group ' + group
		group.enabled = false
		group.locked = false
		if(params.redirect)
			redirect(action:params.redirect, params:[])
		else
			render (view:'group-show', model:[item: group])
	}
	
	def searchGroup = {
		render (view:'groups-search', model:["menuitem" : "searchGroup"]);
	}
	
	def performSearchGroup = {
		def user = injectUserProfile()

		// Default parameters
		if (!params.max) params.max = 15
		if (!params.offset) params.offset = 0
		if (!params.sort) params.sort = "name"
		if (!params.order) params.order = "asc"
		
		def results = groupsService.searchGroups(params.name, params.shortName, params.max,  params.offset,  params.sort,  params.order)
		render results as JSON
	}
	
	def showGroup = {
		def group = Group.findById(params.id)
		def counter = UserGroup.findAllWhere(group: group).size()
		render (view:'group-show', model:[group:group]);
	}
	
	def editGroup = {
		def group = Group.findById(params.id)
		render (view:'group-edit', model:[group:group]);
	}
	
	def createGroup = {
		render (view:'group-create',  model:[action: "create", "menuitem" : "createGroup"]);
	}
	
	def saveGroup = {GroupCreateCommand groupCreateCmd->
		
		def validationFailed = groupsService.validateGroup(groupCreateCmd);
		if(validationFailed) {
			log.error("While Creating Group " + groupCreateCmd.errors)
			groupCreateCmd.errors.allErrors.each { println it }
			render(view:'group-create', model:[group:groupCreateCmd, roles: Role.list(),
						defaultRole: Role.findByAuthority("ROLE_USER")])
		} else {
			def group = groupCreateCmd.createGroup()
			if(group)  {
				def user = injectUserProfile();
				group.createdBy = user;
				groupsService.updateGroupPrivacy(group, groupCreateCmd.groupPrivacy);
				groupsService.updateGroupStatus(group, groupCreateCmd.groupStatus);
	
				println 'lllllll ' + group.id + group.name + group.description + group.privacy+"--"+group.enabled
				if(!group.save()) {
					// Failure in saving
					group.errors.allErrors.each { println it }
					render(view:'group-create', model:[group:groupCreateCmd,
								msgError: 'The group has not been saved successfully'])
				} else {					
					redirect (action:'showGroup', id: group.id, model: [
								msgSuccess: 'Group saved successfully']);
				}
			} else {
				// User already existing
				render(view:'group-create', model:[group:groupCreateCmd,
							msgError: 'A group with this name is already existing'])
			}
		}
	}
	
	def updateGroup = { GroupEditCommand cmd ->
		def validationFailed = groupsService.validateGroup(cmd);
		if (validationFailed) {
			log.error("While Updating Group " + cmd.errors)
		} else {
			def group = Group.findById(params.id);
			log.debug("Updating Group " + group)
			if(group!=null) {
				group.name = params.name;
				group.shortName = params.shortName;
				group.description = params.description;
	
				groupsService.updateGroupStatus(group, params.groupStatus)
				groupsService.updateGroupPrivacy(group, params.groupPrivacy);
				
				render (view:'group-show', model:[label:params.testId, description:params.testDescription, group:group]);
				return;
			}
		}
		render (view:'group-edit', model:[label:params.testId, description:params.testDescription, group:cmd]);
	}
	
	def listGroupUsers = {
		log.debug("Listing users for group " + (params.id?("(id:" + params.id + ")"):"(No id specified)"));
		
		def group = Group.findById(params.id)
		
		if (!params.max) params.max = 10
		if (!params.offset) params.offset = 0
		//if (!params.sort) params.sort = "username"
		//if (!params.order) params.order = "asc"

		def userGroups = UserGroup.findAllByGroup(group, [max: params.max, sort: params.sort, order: params.order, offset: params.offset]);

		render (view:'group-users-list', model:[
			group: group, "userGroups" : userGroups, "usersTotal": userGroups.size(), "usersroles": UserRole.list(), "roles" : Role.list()])
	}
	
	def manageUserGroups = {
		def user = User.findById(params.id)

		if (!params.max) params.max = 15
		if (!params.offset) params.offset = 0
		if (!params.sort) params.sort = "name"
		if (!params.order) params.order = "asc"

		def results = groupsService.listUserGroups(user, params.max, params.offset, params.sort, params.order);

		render (view:'groups-manage', model:["usergroups" : results, "groupsTotal": Group.count(), "menuitem" : "listGroups", "user": user])
	}
	
	def addUserGroups = {
		def user = User.findById(params.id)
		render (view:'groups-user-add', model:["menuitem" : "searchGroup", 'user': user,
			appBaseUrl: request.getContextPath()]);
	}
	
	def enrollUserInGroup = {
		def user = User.findById(params.user)
		def group = Group.findById(params.group)
		
		def ug = new UserGroup(user:user, group:group,
			status: UserStatusInGroup.findByValue(DefaultUserStatusInGroup.ACTIVE.value()));
		
		if(!ug.save(flush: true)) {
			ug.errors.allErrors.each { println it }
		} else {
			ug.roles = []
			ug.roles.add GroupRole.findByAuthority(DefaultGroupRoles.USER.value())
		}
			
		redirect(action:'showUser', params: [id: params.user]);
	}
	
	def enrollOneUserInGroup = {
		
		def group = Group.findById(params.id)
		def user = User.findById(params.user)
		
		if(UserGroup.findByUserAndGroup(user, group)==null) {
			def ug = new UserGroup(user:user, group:group,
				status: UserStatusInGroup.findByValue(DefaultUserStatusInGroup.ACTIVE.value()));
			
			if(!ug.save(flush: true)) {
				ug.errors.allErrors.each { println it }
			} else {
				ug.roles = []
				ug.roles.add GroupRole.findByAuthority(DefaultGroupRoles.USER.value())
			}
		} else {
			println "!!!!!!!!! already a mamber"
		}		
		redirect(action:'listGroupUsers', params: [id: group.id]);
	}
	
	def unenrollUserFromGroup = {
		def user = User.findById(params.id)
		def group = Group.findById(params.group)
		
		def ug = UserGroup.findByUserAndGroup(user, group);
		
		if(ug!=null) {
			ug.delete();
		} else {
			
		}
			
		redirect(action:'listGroupUsers', params: [id: group.id]);
	}
	
	def disableUserInGroup = {
		def user = User.findById(params.id)
		def group = Group.findById(params.group)
		
		def ug = UserGroup.findByUserAndGroup(user, group);
		if(ug!=null) {
			ug.status =  UserStatusInGroup.findByValue(DefaultUserStatusInGroup.SUSPENDED.value());
		} else {
			
		}
		
		redirect(action:'listGroupUsers', params: [id: group.id]);
	}
	
	def enableUserInGroup = {
		def user = User.findById(params.id)
		def group = Group.findById(params.group)
		
		def ug = UserGroup.findByUserAndGroup(user, group);
		if(ug!=null) {
			ug.status =  UserStatusInGroup.findByValue(DefaultUserStatusInGroup.ACTIVE.value());
		} else {
			
		}
		
		redirect(action:'listGroupUsers', params: [id: group.id]);
	}
	
	def lockUserInGroup = {
		def user = User.findById(params.id)
		def group = Group.findById(params.group)
		
		def ug = UserGroup.findByUserAndGroup(user, group);
		if(ug!=null) {
			ug.status =  UserStatusInGroup.findByValue(DefaultUserStatusInGroup.LOCKED.value());
		} else {
			
		}
		
		redirect(action:'listGroupUsers', params: [id: group.id]);
	}
	
	def unlockUserInGroup = {
		def user = User.findById(params.id)
		def group = Group.findById(params.group)
		
		def ug = UserGroup.findByUserAndGroup(user, group);
		if(ug!=null) {
			ug.status =  UserStatusInGroup.findByValue(DefaultUserStatusInGroup.ACTIVE.value());
		} else {
			
		}
		
		redirect(action:'listGroupUsers', params: [id: group.id]);
	}
	
	def editUserInGroup = {
		def user = User.findById(params.id)
		def group = Group.findById(params.group)
		
		def ug = UserGroup.findByUserAndGroup(user, group);
		if(ug!=null) {
			ug.status =  UserStatusInGroup.findByValue(DefaultUserStatusInGroup.ACTIVE.value());
		} else {
			
		}
		
		render (view:'group-user-edit', model:[usergroup:ug]);
	}
	
	def updateUserInGroup = {
		def user = User.findById(params.user)
		def group = Group.findById(params.id)
		
		println params
		
		def ug = UserGroup.findByUserAndGroup(user, group);
		if(ug!=null) {
			groupsService.updateUserInGroupStatus(ug, params.userStatus)
			
			ug.roles.clear();
			groupsService.updateUserRoleInGroup(ug, GroupRole.findByAuthority(DefaultGroupRoles.ADMIN.value()), params.Admin)
			groupsService.updateUserRoleInGroup(ug, GroupRole.findByAuthority(DefaultGroupRoles.MANAGER.value()), params.Manager)
			groupsService.updateUserRoleInGroup(ug, GroupRole.findByAuthority(DefaultGroupRoles.CURATOR.value()), params.Curator)
			groupsService.updateUserRoleInGroup(ug, GroupRole.findByAuthority(DefaultGroupRoles.USER.value()), params.User)
			groupsService.updateUserRoleInGroup(ug, GroupRole.findByAuthority(DefaultGroupRoles.GUEST.value()), params.Guest)
		} else {
			
		}
		redirect(action:'listGroupUsers', params: [id: group.id]);
	}
	
	def enrollUsersInGroup = {
		def group = Group.findById(params.id)
		render (view:'group-users-add', model:["menuitem" : "searchGroup", group: group,
			appBaseUrl: request.getContextPath()]);
	}
	

	
	// ------------------------------------------------------------------------
	//  CS-SYSTEMS:System
	// ------------------------------------------------------------------------
	
	def listSystems = {
		if (!params.max) params.max = 15
		if (!params.offset) params.offset = 0
		if (!params.sort) params.sort = "systemsCount"
		if (!params.order) params.order = "asc"

		def results = systemsService.listSystems(params.max, params.offset, params.sort, params.order);

		render (view:'systems-list', model:["systems" : results[0], "systemsTotal": SystemApi.count(), "systemsCount": results[1], "menuitem" : "listSystems",
			appBaseUrl: request.getContextPath()])
	}
	
	def enableSystem = {
		def system = SystemApi.findById(params.id)
		def userSystem = UserSystemApi.findAllBySystem(system);
		system.enabled = true
		if(params.redirect)
			redirect(action:params.redirect)
		else
			render (view:'system-show', model:[system: system, usersystems: userSystem])
	}

	def disableSystem = {
		def system = SystemApi.findById(params.id)
		def userSystem = UserSystemApi.findAllBySystem(system);
		system.enabled = false
		if(params.redirect)
			redirect(action:params.redirect)
		else
			render (view:'system-show', model:[system: system, usersystems: userSystem])
	}
	
	def deleteSystem = {
		render 'not implemented yet'
	}
	
	def manageUserSystems = {
		def user = User.findById(params.id)

		if (!params.max) params.max = 15
		if (!params.offset) params.offset = 0
		if (!params.sort) params.sort = "name"
		if (!params.order) params.order = "asc"

		def results = systemsService.listUserSystems(user, params.max, params.offset, params.sort, params.order);

		render (view:'systems-manage', model:["userSystems" : results, "systemsTotal": Group.count(), "menuitem" : "listSystems", "user": user])
	}
	
	def manageSystemAdministrators = {
		render 'not implemented yet'
	}
	
	def saveSystem = {SystemApiCreateCommand systemCreateCmd->
		if(systemCreateCmd.hasErrors()) {
			systemCreateCmd.errors.allErrors.each { println it }
			render(view:'system-create', model:[item:systemCreateCmd])
		} else {
			def system = systemCreateCmd.createSystem()
			def user = injectUserProfile();
			println '------------ ' + system
			if(system)  {
				system.createdBy = user;
				
				if(!system.save()) {
					// Failure in saving
					system.errors.allErrors.each { println it }
					render(view:'system-create', model:[item:systemCreateCmd,
								msgError: 'The system has not been saved successfully'])
				} else {
					UserSystemApi.create(user, system, true);			
					redirect (action:'showSystem', id: system.id, model: [
								msgSuccess: 'System saved successfully']);
				}
			} else {
				// User already existing
				render(view:'system-create', model:[item:systemCreateCmd,
							msgError: 'A system with this name is already existing'])
			}
		}
	}	
	
	def editSystem = {
		def system = SystemApi.findById(params.id)
		render (view:'system-edit', model:[system: system, action: "edit"])
	}

	def updateSystem = { SystemApiEditCommand systemEditCmd ->
		if(systemEditCmd.hasErrors()) {
			systemEditCmd.errors.allErrors.each { println it }
			render(view:'system-edit', model:[system:systemEditCmd])
		} else {
			def system = SystemApi.findById(params.id)
			system.name = systemEditCmd.name
			system.shortName = systemEditCmd.shortName
			system.description = systemEditCmd.description

			systemsService.updateSystemStatus(system, params.systemStatus)

			render (view:'system-show', model:[system: system,
				appBaseUrl: request.getContextPath()])
		}
	}
	
	def searchSystem = {
		render (view:'systems-search', model:["menuitem" : "searchSystems"]);
	}
	
	def performSystemSearch = {
		def user = injectUserProfile()

		if (!params.max) params.max = 15
		if (!params.offset) params.offset = 0
		if (!params.sort) params.sort = "name"
		if (!params.order) params.order = "asc"

		def groups = [];
		def groupsCount = [:]
		def usersCount = [:]
		def groupsStatus = [:]
		SystemApi.list().each { agroup ->
			usersCount.put (agroup.id, agroup.users.size())
			groupsCount.put (agroup.id, agroup.groups.size())
			groupsStatus.put (agroup.id, (agroup.enabled?"enabled":"disabled"))
		}

		// Search with no ordering
		def groupCriteria = SystemApi.createCriteria();
		def r = [];

		if(params.name!=null && params.name.trim().length()>0 &&
		params.shortName!=null && params.shortName.trim().length()>0) {
			println 'case 1'
			r = groupCriteria.list {
				maxResults(params.max?.toInteger())
				firstResult(params.offset?.toInteger())
				order(params.sort, params.order)
				and {
					like('name', params.name)
					like('shortName', params.shortName)
				}
			}
		} else if(params.name!=null && params.name.trim().length()>0 &&
		(params.shortName==null || params.shortName.trim().length()==0)) {
			println 'case 2'
			r = groupCriteria.list {
				maxResults(params.max?.toInteger())
				firstResult(params.offset?.toInteger())
				order(params.sort, params.order)
				like('name', params.name)
			}
		} else if((params.name==null || params.name.trim().length()==0) &&
		params.shortName!=null &&  params.shortName.trim().length()>0) {
			println 'case 3'
			r = groupCriteria.list {
				maxResults(params.max?.toInteger())
				firstResult(params.offset?.toInteger())
				order(params.sort, params.order)
				like('shortName', params.shortName)
			}
		} else {
			println 'case 4'
			r = groupCriteria.list {
				maxResults(params.max?.toInteger())
				firstResult(params.offset?.toInteger())
				order(params.sort, params.order)
			}
		}
		groups = r.toList();
		//}


		def groupsResults = []
		groups.each { groupItem ->
			def groupResult = [id:groupItem.id, name:groupItem.name, shortName: groupItem.shortName,
						description: groupItem.description, status: (groupItem.enabled?"enabled":"disabled"), dateCreated: groupItem.dateCreated]
			groupsResults << groupResult
		}

		def paginationResults = ['offset':params.offset+params.max, 'sort':params.sort, 'order':params.order]


		def results = [groups: groupsResults, pagination: paginationResults, groupsCount: groupsCount, usersCount: usersCount]
		render results as JSON
	}
	
	def showSystem = {
		def system = SystemApi.findById(params.id)
		def userSystem = UserSystemApi.findAllBySystem(system);
		render (view:'system-show', model:[system: system, usersystems: userSystem,
			appBaseUrl: request.getContextPath()])
	}

	def createSystem = {
		render (view:'system-create',  model:[action: "create", "menuitem" : "createSystem"]);
	}
	
	def regenerateSystemKey = {
		def system = SystemApi.findById(params.id)
		def userSystem = UserSystemApi.findAllBySystem(system);
		def key = UUID.randomUUID() as String
		system.apikey = key;
		render (view:'system-show', model:[system: system, usersystems: userSystem,
			appBaseUrl: request.getContextPath()])
	}
	
	def regenerateSystemSecretKey = {
		def system = SystemApi.findById(params.id)
		def userSystem = UserSystemApi.findAllBySystem(system);
		def key = UUID.randomUUID() as String
		system.secretkey = key;
		render (view:'system-show', model:[system: system, usersystems: userSystem,
			appBaseUrl: request.getContextPath()])
	}
}
