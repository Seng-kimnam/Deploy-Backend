package _bbu.lawfirmapi.models.DTO.shared.response;

import lombok.Data;

@Data
public class PaginationResponse {
    private Integer page;
    private Integer offset;
    private Integer limit;
    private Integer nextPage;
    private Integer previousPage;
    private Integer firstPage;
    private Integer lastPage;
    private Integer totalCount;

    public PaginationResponse(int page, int size, int totalCount) {
        int totalPage = (totalCount / size) + (totalCount % size > 0 ? 1 : 0);
        Integer nextPage = page < totalPage ? page + 1 : null;
        Integer offset = (page - 1) * size;

        this.page = page;
        this.offset = offset;
        this.limit = size;
        this.nextPage = nextPage;
        this.previousPage = page - 1 > 0 ? page - 1 : null;
        this.firstPage = 1;
        this.lastPage = totalPage;
        this.totalCount = totalCount;
    }
}