/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sgz.TodoApp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/**
 *
 * @author samg.zun
 */
@Configuration
@ComponentScan(
        excludeFilters
        = @ComponentScan.Filter(
                value = CommandLineRunner.class,
                type = FilterType.ASSIGNABLE_TYPE
        )
)
@EnableAutoConfiguration
public class TestAppConfig {

}
