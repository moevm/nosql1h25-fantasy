package org.example.backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmbeddedPerson {
    private String name;
    private Role role;
}
