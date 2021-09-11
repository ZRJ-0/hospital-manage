package com.atguigu.yygh.hosp.repository;

import com.atguigu.yygh.model.hosp.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalRepository extends MongoRepository<Hospital, String> {
    /**
     * 根据hoscode获取医院  查看是否存在
     *
     * @param hoscode
     * @return
     */
    Hospital getHospitalByHoscode(String hoscode);
}
