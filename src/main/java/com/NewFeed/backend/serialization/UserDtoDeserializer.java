package com.NewFeed.backend.serialization;

import com.NewFeed.backend.dto.UserDto;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class UserDtoDeserializer extends StdDeserializer<UserDto> {

    public UserDtoDeserializer() {
        this(null);
    }

    public UserDtoDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public UserDto deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node   = jp.getCodec().readTree(jp);
        Long id         = node.get("id").asLong();
        String username = node.get("username").asText();
        String email    = node.get("email").asText();
        String password = node.get("password").asText();

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        JsonNode authoritiesNode                 = node.get("authorities");
        Iterator<JsonNode> iterator              = authoritiesNode.elements();

        while (iterator.hasNext()) {
            JsonNode authorityNode = iterator.next();
            String authority        = authorityNode.get("authority").asText();
            authorities.add(new SimpleGrantedAuthority(authority));
        }

        return new UserDto(id, username, email, password, authorities);
    }
}
