package com.poliglota.model.mongo;

import java.util.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.mongodb.core.index.Indexed;

@Data
@Document(collection = "groups")
@NoArgsConstructor
@AllArgsConstructor
public class Group {

    @Id
    private Long id;

	@Indexed(unique = true)
    private String name;

    private List<Long> memberIds = new ArrayList<>();
}
