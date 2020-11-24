package com.sepehr.sepehr.repository;



import com.sepehr.sepehr.common.data.Searchable;
import com.sepehr.sepehr.common.data.SearchableFieldType;
import com.sepehr.sepehr.common.data.SearchableOperation;
import com.sepehr.sepehr.common.data.SearchableType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.*;
import java.util.stream.Collectors;

public class SearchableSpecification<T> implements Specification<T> {

    private final List<Searchable> dtos;

    private static Logger logger = LoggerFactory.getLogger(SearchableSpecification.class);

    public SearchableSpecification(List<Searchable> dtos) {
        this.dtos = dtos;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicates = new ArrayList<>();

        for (Searchable dto : dtos) {
            logger.info("Processing search dto " + dto);

            if (dto.getFieldType() == SearchableFieldType.JOIN) {

                String[] token = dto.getField().split("\\.");
                Join join = root.join(token[0]);
                predicates.addAll(getPredicates(criteriaBuilder, dto, join.get(token[1])));

            } else if (dto.getFieldType() == SearchableFieldType.COLLECTION) {

                Path collection = root.get(dto.getField());
                predicates.add(criteriaBuilder.isMember(dto.getValue(), collection));

            } else {
                if (dto.getOperation() == SearchableOperation.IN) {
                    Expression<String> expression = root.get(dto.getField());
                    String[] list =(String[]) dto.getValue();

                    if (dto.getType()== SearchableType.LONG) {
                        List<Long> value = Arrays.stream(list).map(Long::parseLong).collect(Collectors.toList());
                        predicates.add(expression.in(value));

                    } else if (dto.getType()==SearchableType.STRING) {
                        List<String> value = Arrays.stream(list).map(String::toString).collect(Collectors.toList());
                        predicates.add(expression.in(value));
                    }

                } else {
                    predicates.addAll(getPredicates(criteriaBuilder, dto, getExpression(root, dto)));
                }
            }
        }

        Predicate[] p = new Predicate[predicates.size()];
        for (int i = 0; i < predicates.size(); i++) {
            p[i] = predicates.get(i);
        }
        return criteriaBuilder.and(p);
    }

    private Path getExpression(Root<T> root, Searchable dto) {
        Path result = root;
        StringTokenizer tokenizer = new StringTokenizer(dto.getField(), ".");
        while (tokenizer.hasMoreTokens()) {
            result = result.get(tokenizer.nextToken());
        }

        return result;
    }


    private List<Predicate> getPredicates(CriteriaBuilder criteriaBuilder, Searchable dto, Expression expression) {

        List<Predicate> predicates = new ArrayList<>();
        if (dto.getOperation() == SearchableOperation.EQUALS) {
            predicates.add(criteriaBuilder.equal(expression, dto.getValue()));

        } else if (dto.getOperation() == SearchableOperation.LIKE) {
            if (StringUtils.isBlank(String.valueOf(dto.getValue())) || String.valueOf(dto.getValue()).length() < 3)
                logger.info("Passed value is too short to be used for like operation: " + dto.getValue());
            else
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(expression),
                        "%" + StringUtils.lowerCase(dto.getValue().toString()) + "%"));

        } else if (dto.getOperation() == SearchableOperation.BIGGER) {
            if (dto.getDataClass().equals(Date.class))
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(expression, (Date) dto.getValue()));
            else if (dto.getDataClass().equals(Integer.class))
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(expression, (Integer) dto.getValue()));
            else if (dto.getDataClass().equals(Long.class))
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(expression, (Long) dto.getValue()));
            else if (dto.getDataClass().equals(String.class))
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(expression, (String) dto.getValue()));

        } else if (dto.getOperation() == SearchableOperation.LESSER) {
            if (dto.getDataClass().equals(Date.class))
                predicates.add(criteriaBuilder.lessThanOrEqualTo(expression, (Date) dto.getValue()));
            else if (dto.getDataClass().equals(Integer.class))
                predicates.add(criteriaBuilder.lessThanOrEqualTo(expression, (Integer) dto.getValue()));
            else if (dto.getDataClass().equals(Long.class))
                predicates.add(criteriaBuilder.lessThanOrEqualTo(expression, (Long) dto.getValue()));
            else if (dto.getDataClass().equals(String.class))
                predicates.add(criteriaBuilder.lessThanOrEqualTo(expression, (String) dto.getValue()));
        }
        return predicates;
    }
}

