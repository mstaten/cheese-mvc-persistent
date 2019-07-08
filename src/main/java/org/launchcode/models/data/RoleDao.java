package org.launchcode.models.data;

import org.launchcode.models.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface RoleDao extends CrudRepository<Role, Integer> {

    Role findByName(String name);

    @Override
    void delete(Role role);

}
