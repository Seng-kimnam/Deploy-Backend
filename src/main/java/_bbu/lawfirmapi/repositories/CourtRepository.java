package _bbu.lawfirmapi.repositories;

import _bbu.lawfirmapi.models.Entity.Court;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourtRepository extends JpaRepository<Court , Long> {
}
