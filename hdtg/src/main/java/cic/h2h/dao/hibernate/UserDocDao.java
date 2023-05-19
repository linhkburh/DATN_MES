package cic.h2h.dao.hibernate;

import org.springframework.stereotype.Repository;

import entity.frwk.UserDocumentation;
@Repository(value = "userDocDao")
public class UserDocDao extends H2HBaseDao<UserDocumentation>{

}
