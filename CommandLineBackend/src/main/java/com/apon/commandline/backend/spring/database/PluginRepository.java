package com.apon.commandline.backend.spring.database;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PluginRepository extends CrudRepository<Plugin, Long> {
    List<Plugin> findByPluginIdentifier(String pluginIdentifier);
}
