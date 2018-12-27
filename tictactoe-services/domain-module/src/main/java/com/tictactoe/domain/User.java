/*
 * © Copyright 2018 Aleksey Popryadukhin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions
 * of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.tictactoe.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * User: aleksey
 * Date: 2018-11-29
 * Time: 05:05
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("user")
public class User implements UserDetails {

  @Id
  private String id;
  private String email;
  private String password;
  private Integer score;
  @JsonIgnore
  private String fingerprint;
  /**
   * True - means user is not temporary, False - means system user
   */
  @JsonIgnore
  @Builder.Default()
  private boolean guest = true;

  @JsonIgnore
  @Builder.Default()
  private boolean active = true;
  @JsonIgnore
  @Builder.Default()
  private List<String> authorities = new ArrayList<>();

  @JsonCreator
  public User(@JsonProperty("email") String email, @JsonProperty("score") Integer score) {
    this.email = email;
    this.score = score;
    this.authorities = Objects.requireNonNullElseGet(authorities, ArrayList::new);
  }

  @JsonIgnore
  @Override
  public String getUsername() {
    return email;
  }

  public void setUsername(String email) {
    this.email = email;
  }

  @JsonIgnore
  @Override
  public String getPassword() {
    return password;
  }

  @JsonIgnore
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return AuthorityUtils.createAuthorityList(authorities.toArray(new String[0]));
  }

  public void addAuthority(String authority) {
    authorities.add(authority);
  }

  public void retainAuthorities(List<String> authorities) {
    this.authorities.retainAll(authorities);
  }

  @JsonIgnore
  @Override
  public boolean isAccountNonExpired() {
    return active;
  }

  @JsonIgnore
  @Override
  public boolean isAccountNonLocked() {
    return active;
  }

  @JsonIgnore
  @Override
  public boolean isCredentialsNonExpired() {
    return active;
  }

  @JsonIgnore
  @Override
  public boolean isEnabled() {
    return active;
  }

  public void loggedIn() {
    // spy your users 😄
  }

  public void loggedOut() {
    // spy your users 😄
  }
}
