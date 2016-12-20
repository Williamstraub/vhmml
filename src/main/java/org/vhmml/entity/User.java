package org.vhmml.entity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "users")
@Where(clause="removed = 0")
public class User implements UserDetails {	

	private static final long serialVersionUID = -835238351122850290L;

	@Id
	@GeneratedValue
	private Long id;
	private String uuid;
	
	private String username;
	
	@JsonIgnore	
	private String password;	
	
	private Boolean enabled;

	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "register_time")
	private Date registerTime;
	
	@Column(name = "first_login")
	private Date firstLogin;
	
	@Column(name = "last_login")
	private Date lastLogin;
	
	private String institution;
	
	@Column(name = "professional_title")
	private String professionalTitle;	
	
	private String address;
	private String phone;
	
	@Column(name = "research_interests")
	private String researchInterests;
	
	@Column(name = "region_lang_interests")
	private String regionAndLangInterests;
	
	@Column(name = "credentials_expired")
	private Boolean credentialsExpired;
	
	@Column(name = "failed_logins")
	private Integer failedLogins;
	
	@Column(name = "last_failed_login")
	private Date lastFailedLogin;
	
	@Column(name = "account_locked")
	private Boolean accountLocked;
	
	@Column(name = "internal_notes")
	private String internalNotes;
	
	@Column(name = "usage_agreement_accepted")
	private boolean usageAgreementAccepted;
	
	@Column(name = "usage_agreement_accepted_date")
	private Date usageAgreementAcceptedDate;
	
	private boolean removed;
	
	@OneToMany(fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private List<Role> roles;

	public User() {
		super();
	}
	
	public User(String username, String password, Boolean enabled) {
		this.username = username;
		this.password = password;
		this.enabled = enabled;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	public Date getFirstLogin() {
		return firstLogin;
	}

	public void setFirstLogin(Date firstLogin) {
		this.firstLogin = firstLogin;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}
	
	public String getLastLoginDisplay() {
		String lastLoginDisplay = "";
		
		if(lastLogin != null) {
			return new SimpleDateFormat("yyyy/MM/dd 'at' hh:mm aaa z").format(lastLogin);
		}		
		
		return lastLoginDisplay;
	}

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public String getProfessionalTitle() {
		return professionalTitle;
	}

	public void setProfessionalTitle(String professionalTitle) {
		this.professionalTitle = professionalTitle;
	}
	
	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getResearchInterests() {
		return researchInterests;
	}

	public void setResearchInterests(String researchInterests) {
		this.researchInterests = researchInterests;
	}

	public String getRegionAndLangInterests() {
		return regionAndLangInterests;
	}

	public void setRegionAndLangInterests(String regionAndLangInterests) {
		this.regionAndLangInterests = regionAndLangInterests;
	}

	public Boolean getCredentialsExpired() {
		return credentialsExpired;
	}

	public void setCredentialsExpired(Boolean credentialsExpired) {
		this.credentialsExpired = credentialsExpired;
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	public Integer getFailedLogins() {
		return failedLogins;
	}

	public void setFailedLogins(Integer failedLogins) {
		this.failedLogins = failedLogins;
	}

	public Date getLastFailedLogin() {
		return lastFailedLogin;
	}

	public void setLastFailedLogin(Date lastFailedLogin) {
		this.lastFailedLogin = lastFailedLogin;
	}
	
	public Boolean getAccountLocked() {
		return accountLocked;
	}

	public void setAccountLocked(Boolean accountLocked) {
		this.accountLocked = accountLocked;
	}
	
	public String getInternalNotes() {
		return internalNotes;
	}

	public void setInternalNotes(String internalNotes) {
		this.internalNotes = internalNotes;
	}
	
	public boolean hasRole(List<Role.Name> roleNames) {
		boolean hasRole = false;
		
		for(Role.Name roleName : roleNames) {
			if(hasRole(roleName)) {
				hasRole = true;
				break;
			}
		}
		
		return hasRole;
	}
	
	public boolean hasRole(Role.Name roleName) {
		boolean hasRole = false;
		
		Collection<? extends GrantedAuthority> auths = getAuthorities();
		
		for(GrantedAuthority auth : auths) {
			if(roleName.name().equals(auth.getAuthority())) {
				hasRole = true;
				break;
			}
		}
		
		return hasRole;
	}	
	
	public boolean isUsageAgreementAccepted() {
		return usageAgreementAccepted;
	}

	public void setUsageAgreementAccepted(boolean usageAgreementAccepted) {
		this.usageAgreementAccepted = usageAgreementAccepted;
	}
	
	/* the following methods fulfill the UserDetails interface and are called by Spring Security */ 
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<String> roleNames = new ArrayList<String>();
		
		if(CollectionUtils.isNotEmpty(roles)) {
			for(Role role : roles) {
				roleNames.add(role.getName());
			}
		}
		
		return AuthorityUtils.createAuthorityList(roleNames.toArray(new String[roleNames.size()]));
	}

	public boolean isAccountNonExpired() {
		return true;
	}

	public boolean isAccountNonLocked() {
		return accountLocked == null || !accountLocked;
	}

	public boolean isCredentialsNonExpired() {
		return true;
	}

	public Date getUsageAgreementAcceptedDate() {
		return usageAgreementAcceptedDate;
	}

	public void setUsageAgreementAcceptedDate(Date usageAgreementAcceptedDate) {
		this.usageAgreementAcceptedDate = usageAgreementAcceptedDate;
	}

	public boolean getRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}
	
	/* end UserDetails interface methods */
}
