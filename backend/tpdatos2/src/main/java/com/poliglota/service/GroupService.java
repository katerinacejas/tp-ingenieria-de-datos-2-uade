package com.project.service;

import com.project.model.mongodb.Group;
import com.project.repository.mongodb.GroupRepository;
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
        if (groupRepository.existsById(id)) {
            groupRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
