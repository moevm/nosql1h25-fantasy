package org.example.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.backend.model.Role;

@Data
@NoArgsConstructor
public class EmbeddedPersonDto {
    private String name;
    private Role role;
}
