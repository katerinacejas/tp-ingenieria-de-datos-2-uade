package com.poliglota.service;

import com.poliglota.model.mongo.Group;
import com.poliglota.repository.GroupRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public Group getGroupByName(String name) {
        return groupRepository.findByGroupbyName(name);
    }

    public Group saveGroup(Group group) {
        return groupRepository.save(group);
    }

    public boolean deleteGroup(String id) {
        try {
            Long longId = Long.parseLong(id);
            if (groupRepository.existsById(longId)) {
                groupRepository.deleteById(longId);
                return true;
            }
        } catch (NumberFormatException e) {
            // Invalid id format
        }
        return false;
    }
}
