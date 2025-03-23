package com.att.tdp.popcorn_palace.DataAccessLayer.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.att.tdp.popcorn_palace.DataAccessLayer.Entity.MovieEntity;

@Repository
public interface MovieRepository extends JpaRepository<MovieEntity,String>{
    
}
