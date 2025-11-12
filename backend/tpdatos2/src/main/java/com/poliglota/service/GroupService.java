package com.poliglota.service;

import com.poliglota.model.mongo.Group;
import com.poliglota.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GroupService {

    private final GroupRepository groupRepository;

    // Crear un nuevo grupo
    public Group createGroup(Group group) {
        if (groupRepository.findByName(group.getName()) != null) {
            throw new IllegalArgumentException("Ya existe un grupo con el nombre: " + group.getName());
        }
        return groupRepository.save(group);
    }

    // Obtener todos los grupos
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    // Buscar grupo por ID
    public Group getGroupById(Long id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado con ID: " + id));
    }

    // Buscar grupo por nombre
    public Group getGroupByName(String name) {
        return groupRepository.findByName(name);
    }

    // Agregar miembro
    public Group addMember(Long groupId, Long userId) {
        Group group = getGroupById(groupId);
        if (!group.getMemberIds().contains(userId)) {
            group.getMemberIds().add(userId);
        }
        return groupRepository.save(group);
    }

    // Eliminar miembro
    public Group removeMember(Long groupId, Long userId) {
        Group group = getGroupById(groupId);
        group.getMemberIds().remove(userId);
        return groupRepository.save(group);
    }

    // Eliminar grupo
    public void deleteGroup(Long id) {
        groupRepository.deleteById(id);
    }
}
