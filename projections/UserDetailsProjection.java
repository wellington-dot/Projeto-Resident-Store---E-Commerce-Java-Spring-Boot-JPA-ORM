package com.wsystems.residentstore.projections;

public interface UserDetailsProjection {

    //getUsername(), está como não usado pq usamos o atributo "username"
    String getUsername();
    String getPassword();
    Long getRoleId();
    String getAuthority();

}
