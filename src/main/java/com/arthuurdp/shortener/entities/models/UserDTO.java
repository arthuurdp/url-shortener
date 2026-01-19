package com.arthuurdp.shortener.entities.models;

import java.io.Serializable;

public record UserDTO(Long id, String name, String email) implements Serializable {
}
