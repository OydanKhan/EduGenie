package com.usyd.edugenie.repository;

import com.usyd.edugenie.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface TagRepository extends JpaRepository<Tag, UUID> {

}
