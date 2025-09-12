    package com.example.chat.member.entity;

    import com.example.chat.auth.Role;
    import lombok.Builder;
    import lombok.Getter;
    import jakarta.persistence.*;
    import lombok.NoArgsConstructor;

    @Getter
    @NoArgsConstructor
    @Entity
    @Table(name = "members")
    public class Member {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "member_id")
        private Long memberId;

        @Column(nullable = false)
        private String loginId;

        @Column(nullable = false)
        private String username;

        @Column(nullable = false)
        private String email;

        @Enumerated(EnumType.STRING)
        private Role role;

        private String provider;

        private String providerId;

        @Enumerated(EnumType.STRING)
        private MemberStatus memberStatus;



        @Builder
        private Member(String username, String loginId, String email, Role role, String provider, String providerId, MemberStatus memberStatus) {
            this.username = username;
            this.loginId = loginId;
            this.email = email;
            this.role = role;
            this.provider = provider;
            this.providerId = providerId;
            this.memberStatus = memberStatus;
        }

    }
