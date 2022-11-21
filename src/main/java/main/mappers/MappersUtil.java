package main.mappers;

import main.model.entities.Like;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public class MappersUtil {

    @Named("getLikesCount")
    public int getLikesCount(List<Like> likes) {
        return likes == null ? 0 : likes.size();
    }
}
