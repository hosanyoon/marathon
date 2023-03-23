package com.ssafy.marathon.db.entity.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@Entity
@Table(name = "user")
public class User implements UserDetails {

    @Id
    @GeneratedValue
    private Long seq;
    @Column(nullable = false, unique = true)
    private String id;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    // Json 결과로 출력하지 않을 데이터에 대해 해당 어노테이션 설정 값 추가
    //lombok을 사용하고 있다면 제 생각엔 @JsonProperty를 사용하는것 접근을 제어하는데 유리
    @Column(nullable = false)
    private String password;
    private String name;
    //남 : 1(true),  여 : 0(false)
    private boolean sex;
    private String email;
    private String phone;
    private String img;
    private LocalDate birthDate;
    private String kakao;
    @Column(updatable = false, insertable = false)
    private String dtype;
    @Column(updatable = false)
    private LocalDate registDate;

    public void updateUser(String password, String email, String phone) {
        this.password = password;
        this.email = email;
        this.phone = phone;
    }

    public void updateUserPassword(String password) {
        this.password = password;
    }

    public void updateUserKakao(String kakao) {
        this.kakao = kakao;
    }

    public void updateUserImg(String img) {
        this.img = img;
    }

    @ElementCollection(fetch = FetchType.EAGER)
    //컬렉션과 같은 형태의 데이터를 컬럼에 저장할 수 없기 때문에, 별도의 테이블을 생성하여 컬렉션을 관리해야 합니다.
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public String getUsername() {
        return this.id;
    }

    /**
     * 계정이 만료되었는지 체크하는 로직 이 예제에서는 사용하지 않으므로 true 값 return
     *
     * @return true
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 계정이 잠겼는지 체크하는 로직 이 예제에서는 사용하지 않으므로 true 값 return
     *
     * @return true
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 계정의 패스워드가 만료되었는지 체크하는 로직 이 예제에서는 사용하지 않으므로 true 값 return
     *
     * @return true
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 계정이 사용가능한지 체크하는 로직 이 예제에서는 사용하지 않으므로 true 값 return
     *
     * @return true
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isEnabled() {
        return true;
    }
}