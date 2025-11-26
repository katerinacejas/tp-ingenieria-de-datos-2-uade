package com.poliglota.controller;

import com.poliglota.DTO.GroupDTO;
import com.poliglota.model.mongo.Group;
import com.poliglota.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    //  Crear grupo
    @PostMapping
    public ResponseEntity<GroupDTO> createGroup(  GroupDTO groupDTO) {
        return ResponseEntity.ok(groupService.createGroup(groupDTO));
    }

    //  Obtener todos los grupos de un usuario
    @GetMapping
    public ResponseEntity<List<GroupDTO>> getAllGroupsByMemberId(   Long memberId) {
        return ResponseEntity.ok(groupService.getAllGroupsByMemberId(memberId));
    }

    //  Buscar grupo por ID
    @GetMapping("/{id}")
    public ResponseEntity<GroupDTO> getGroupById(  String id) {
        return ResponseEntity.ok(groupService.getGroupById(id));
    }

    //  Buscar grupo por nombre
    @GetMapping("/name/{name}")
    public ResponseEntity<GroupDTO> getGroupByName(  String name) {
        return ResponseEntity.ok(groupService.getGroupByName(name));
    }

    //  Agregar un miembro al grupo
    @PostMapping("/{groupId}/add-member/{userId}")
    public ResponseEntity<GroupDTO> addMember(  String groupId,   Long userId) {
        return ResponseEntity.ok(groupService.addMember(groupId, userId));
    }

    //  Eliminar un miembro del grupo
    @PostMapping("/{groupId}/remove-member/{userId}")
    public ResponseEntity<GroupDTO> removeMember(  String groupId,   Long userId) {
        return ResponseEntity.ok(groupService.removeMember(groupId, userId));
    }

}
