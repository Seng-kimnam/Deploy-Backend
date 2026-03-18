package _bbu.lawfirmapi.repositories;

import _bbu.lawfirmapi.models.DTO.expertise.response.ExpertiseResponse;
import _bbu.lawfirmapi.models.Entity.Expertise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface ExpertiseRepository extends JpaRepository<Expertise , Integer> {

//    List<Expertise> findAllExpertise();
}
