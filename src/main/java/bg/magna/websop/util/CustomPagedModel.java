package bg.magna.websop.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.web.PagedModel;

import java.util.List;

public class CustomPagedModel<T> {
    private List<T> content;
    private PagedModel.PageMetadata page;

    public CustomPagedModel() {
    }

    public CustomPagedModel(List<T> content, PagedModel.PageMetadata page) {
        this.content = content;
        this.page = page;
    }

    @JsonProperty
    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    @JsonProperty
    public PagedModel.PageMetadata getPage() {
        return page;
    }

    public void setPage(PagedModel.PageMetadata page) {
        this.page = page;
    }

    public boolean hasPrevious() {
        return page.number() > 0;
    }

    public boolean hasNext() {
        return page.number() < page.totalPages() - 1;
    }
}
