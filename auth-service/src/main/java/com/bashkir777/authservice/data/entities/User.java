package com.bashkir777.authservice.data.entities;


import com.bashkir777.authservice.data.tools.PasswordConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Convert(converter = PasswordConverter.class)
    private String password;

    @Email
    @Column(unique = true)
    private String email;

    @Size(min=3, max=20)
    private String firstname;

    @Size(min=3, max=20)
    private String lastname;

    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean disabled;
}
