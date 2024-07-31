package bg.magna.websop.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PagedModelDeserializer<T> extends JsonDeserializer<PagedModel<T>> {

    private final Class<T> contentType;
    private final ObjectMapper objectMapper;

    public PagedModelDeserializer(Class<T> contentType, ObjectMapper objectMapper) {
        this.contentType = contentType;
        this.objectMapper = objectMapper;
    }

    @Override
    public PagedModel<T> deserialize(JsonParser p, DeserializationContext context)
            throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);

        JsonNode pageMetadataNode = node.get("page");
        JsonNode contentNode = node.get("content");

        // Create PageMetadata object
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(
                pageMetadataNode.get("size").asInt(),
                pageMetadataNode.get("number").asInt(),
                pageMetadataNode.get("totalElements").asLong(),
                pageMetadataNode.get("totalPages").asInt()
        );

        // Deserialize content
        List<T> content = new ArrayList<>();
        if (contentNode != null) {
            Iterator<JsonNode> elements = contentNode.elements();
            while (elements.hasNext()) {
                JsonNode element = elements.next();
                T item = objectMapper.treeToValue(element, contentType);
                content.add(item);
            }
        }

        // Create Pageable object from PageMetadata
        Pageable pageable = PageRequest.of(
                (int) pageMetadata.number(),
                (int) pageMetadata.size());

        // Create PageImpl to wrap the content
        PageImpl<T> page = new PageImpl<>(content, pageable, pageMetadata.totalElements());

        // Wrap into PagedModel
        return new PagedModel<>(page);
    }
}
