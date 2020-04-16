package com.ofs.web.filter;

import com.ofs.web.shiro.ShiroConfig;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;

/**
 * 扫描过滤掉shiro 的过滤器
 */
public class MyTypeFilter implements TypeFilter {
    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        if (metadataReader.getAnnotationMetadata().isAnnotated(ShiroConfig.class.getName())) {
            return true;
        }

        ClassMetadata classMetadata = metadataReader.getClassMetadata();
        String claseName = classMetadata.getClassName();
        if (claseName.contains("com.ofs.web.shiro")) {
            return true;
        }
        return false;
    }
}