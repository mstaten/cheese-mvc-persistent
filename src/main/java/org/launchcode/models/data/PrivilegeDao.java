package org.launchcode.models.data;

import org.launchcode.models.Privilege;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface PrivilegeDao extends CrudRepository<Privilege, Integer> {

    Privilege findByName(String name);

    @Override
    void delete(Privilege privilege);
}
