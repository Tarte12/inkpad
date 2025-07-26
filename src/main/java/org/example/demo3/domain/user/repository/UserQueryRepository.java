package org.example.demo3.domain.user.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.demo3.domain.user.QUser;
import org.example.demo3.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Page<User> searchByKeyword(String keyword, Pageable pageable) {
        QUser user = QUser.user;

        List<User> users = queryFactory
                .selectFrom(user)
                .where(
                        containsUsernameOrNickname(keyword)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(user.id.desc())
                .fetch();

        Long total = queryFactory
                .select(user.count())
                .from(user)
                .where(
                        containsUsernameOrNickname(keyword)
                )
                .fetchOne();

        return new PageImpl<>(users, pageable, total);
    }

    private BooleanExpression containsUsernameOrNickname(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return null;
        }
        QUser user = QUser.user;
        return user.username.containsIgnoreCase(keyword)
                .or(user.nickname.containsIgnoreCase(keyword));
    }
}
