package com.poliglota.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.poliglota.service.GroupService;
import com.poliglota.model.mongo.Group;
import java.util.List;

@RestController
@RequestMapping("/api/group")
@RequiredArgsConstructor
public class GroupController {
	private final GroupService groupService;

	@PostMapping
	public ResponseEntity<Group> create(@RequestBody group ) {
		return ResponseEntity.ok(GroupService.create(group));
	}

	@GetMapping
	public ResponseEntity<List<Group>> list() {
		return ResponseEntity.ok(GroupService.getAll());
	}

    @DeleteMapping
	public ResponseEntity<List<Group>> delete() {
		return ResponseEntity.ok(GroupService.delete(group));
	}
}
