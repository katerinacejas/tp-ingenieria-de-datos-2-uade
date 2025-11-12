package com.poliglota.controller;

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
    public ResponseEntity<Group> createGroup(@RequestBody Group group) {
        return ResponseEntity.ok(groupService.createGroup(group));
    }

    //  Obtener todos los grupos
    @GetMapping
    public ResponseEntity<List<Group>> getAllGroups() {
        return ResponseEntity.ok(groupService.getAllGroups());
    }

    //  Buscar grupo por ID
    @GetMapping("/{id}")
    public ResponseEntity<Group> getGroupById(@PathVariable Long id) {
        return ResponseEntity.ok(groupService.getGroupById(id));
    }

    //  Buscar grupo por nombre
    @GetMapping("/name/{name}")
    public ResponseEntity<Group> getGroupByName(@PathVariable String name) {
        return ResponseEntity.ok(groupService.getGroupByName(name));
    }

    //  Agregar un miembro al grupo
    @PostMapping("/{groupId}/add-member/{userId}")
    public ResponseEntity<Group> addMember(@PathVariable Long groupId, @PathVariable Long userId) {
        return ResponseEntity.ok(groupService.addMember(groupId, userId));
    }

    //  Eliminar un miembro del grupo
    @PostMapping("/{groupId}/remove-member/{userId}")
    public ResponseEntity<Group> removeMember(@PathVariable Long groupId, @PathVariable Long userId) {
        return ResponseEntity.ok(groupService.removeMember(groupId, userId));
    }

    //  Eliminar un grupo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        groupService.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }
}
