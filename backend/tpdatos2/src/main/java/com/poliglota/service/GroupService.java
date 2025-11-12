package com.poliglota.service;

import com.poliglota.DTO.GroupDTO;
import com.poliglota.model.mongo.Group;
import com.poliglota.repository.mongo.GroupRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final MongoTemplate mongoTemplate;

    public GroupDTO createGroup(GroupDTO dto) {
        String name = Optional.ofNullable(dto.getName())
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .orElseThrow(() -> new IllegalArgumentException("El nombre del grupo es requerido."));

        if (groupRepository.existsByName(name)) {
            throw new DuplicateKeyException("Ya existe un grupo con el nombre: " + name);
        }

        List<Long> members = Optional.ofNullable(dto.getMemberIds()).orElseGet(ArrayList::new);
        // de-duplicar manteniendo orden de inserción
        members = new ArrayList<>(new LinkedHashSet<>(members));

        Group g = new Group(null, name, members);
        return toDTO(groupRepository.save(g));
    }

    public List<GroupDTO> getAllGroupsByMemberId(Long memberId) {
        return groupRepository.findByMemberIds(memberId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public GroupDTO getGroupById(String id) {
        Group g = groupRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Grupo no encontrado: " + id));
        return toDTO(g);
    }

    public GroupDTO getGroupByName(String name) {
        Group g = groupRepository.findByName(name)
                .orElseThrow(() -> new NoSuchElementException("Grupo no encontrado: " + name));
        return toDTO(g);
    }

    public GroupDTO addMember(String groupId, Long userId) {
        Query q = Query.query(Criteria.where("_id").is(groupId));
        Update u = new Update().addToSet("memberIds", userId); // $addToSet evita duplicados
        Group updated = mongoTemplate.findAndModify(
                q, u,
                FindAndModifyOptions.options().returnNew(true),
                Group.class
        );
        if (updated == null) throw new NoSuchElementException("Grupo no encontrado: " + groupId);
        return toDTO(updated);
    }

    public GroupDTO removeMember(String groupId, Long userId) {
        Query q = Query.query(Criteria.where("_id").is(groupId));
        Update u = new Update().pull("memberIds", userId); // $pull elimina el valor del array
        Group updated = mongoTemplate.findAndModify(
                q, u,
                FindAndModifyOptions.options().returnNew(true),
                Group.class
        );
        if (updated == null) throw new NoSuchElementException("Grupo no encontrado: " + groupId);
        return toDTO(updated);
    }


    private GroupDTO toDTO(Group g) {
        GroupDTO dto = new GroupDTO();
        dto.setId(g.getId());
        dto.setName(g.getName());
        dto.setMemberIds(g.getMemberIds() != null ? List.copyOf(g.getMemberIds()) : List.of());
        return dto;
    }
}
