package com.sepehr.sepehr.common.data;

import com.sepehr.sepehr.common.exceptions.ErrorCodes;
import com.sepehr.sepehr.common.exceptions.ServiceException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

public class SearchUtils {

    private static Logger logger = Logger.getLogger("SearchUtils");
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public static List<Searchable> createSearchableFrom(String filters, Searchable[] searchables) throws ServiceException {
        logger.info("Query string is " + filters);

        List<Searchable> compiled = new ArrayList<Searchable>();

        StringTokenizer tokenizer = new StringTokenizer(filters, "&");
        while (tokenizer.hasMoreTokens()) {
            String item = tokenizer.nextToken();
            logger.info("Found " + item + " while traversing for parameters");

            if (!item.contains("=")) {
                logger.info("Cannot find the separator");
                continue;
            }

            String paramName = item.substring(0, item.indexOf('='));
            String paramValue = item.substring(item.indexOf('=') + 1);
            logger.info("Broke the item to " + paramName + " and " + paramValue);

            processParameter(searchables, compiled, paramName, paramValue);
        }

        return compiled;
    }

    public static List<Searchable> createSearchableFrom(Map<String, String> filters, Searchable[] searchables) throws ServiceException {

        List<Searchable> compiled = new ArrayList<Searchable>();

        for (String item : filters.keySet()) {
            String paramValue = filters.get(item);
            logger.info("Found parameters " + item + " == " + paramValue);

            processParameter(searchables, compiled, item, paramValue);
        }

        return compiled;
    }

    private static void processParameter(Searchable[] searchables, List<Searchable> compiled, String paramName, String paramValue) throws ServiceException {
        Searchable dto = findSearchDtoFor(paramName, searchables);
        if (dto == null) {
            logger.info("Parameter is not supported : " + paramName);
            return;
        }

        if (StringUtils.isBlank(paramValue)) {
            logger.info("Parameter value is empty : " + paramName);
            return;
        }

        Object value = null;

        if (dto.getOperation() == SearchableOperation.IN) {
            String[] list = paramValue.toString().split(",");
            value = list;
        } else {

            try {
                switch (dto.getType()) {
                    case DATE:
                        value = dateFormat.parse(paramValue);
                        dto.setDataClass(Date.class);
                        break;

                    case ENUMERATION:
                        if (dto.getDataClass() == null)
                            logger.warning("Data class in search dto is required for enumeration parameters");
                        else {
                            value = Enum.valueOf(dto.getDataClass(), paramValue);
                        }
                        break;

                    case INTEGER:
                        value = Integer.valueOf(paramValue);
                        dto.setDataClass(Integer.class);
                        break;

                    case LONG:
                        value = Long.valueOf(paramValue);
                        dto.setDataClass(Long.class);
                        break;

                    case STRING:
                        value = paramValue;
                        dto.setDataClass(String.class);
                        break;

                    case BIG_DECIMAL:
                        value = new BigDecimal(paramValue);
                        dto.setDataClass(BigDecimal.class);
                        break;

                    case OBJECT:
                        value = paramValue;
                        dto.setDataClass(Object.class);
                        break;
                }
            } catch (Throwable e) {
                throw new ServiceException(paramName + " is not valid", ErrorCodes.BAD_INPUT.getCode());
            }
        }
        compiled.add(new Searchable(paramName, dto.getField(), dto.getOperation(), dto.getType(), dto.getFieldType(), value).setDataClass(dto.getDataClass()));
    }


    private static Searchable findSearchDtoFor(String key, Searchable[] searchables) {
        for (Searchable dto : searchables)
            if (StringUtils.equalsIgnoreCase(key, dto.getName()))
                return dto;

        return null;
    }


    public static SearchablePage getSearchableResult(SearchablePage searchablePage, Searchable[] searchables) throws ServiceException {
        PageRequest pageRequest = getPageRequest(searchablePage);
        List<Searchable> searchableFrom = createSearchableFrom(searchablePage.getFilter(), searchables);

        return SearchablePage.builder().pageRequest(pageRequest).searchables(searchableFrom).build();
    }

    public static PageRequest getPageRequest(SearchablePage searchablePage) throws ServiceException {
        Sort.Direction dir = getDirection(searchablePage.getDirection());
        return PageRequest.of(searchablePage.getPage(), searchablePage.getTotal(), dir, searchablePage.getOrder());
    }


    private static Sort.Direction getDirection(String direction) throws ServiceException {
        Sort.Direction dir;
        try {
            dir = Sort.Direction.fromString(direction);
        } catch (Exception exp) {
            exp.printStackTrace();
            throw new ServiceException("Please make sure direction is valid ", ErrorCodes.INTERNAL_ERROR.getCode());
        }
        return dir;
    }
}
