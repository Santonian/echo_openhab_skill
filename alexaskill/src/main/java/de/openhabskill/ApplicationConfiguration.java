package de.openhabskill;

import org.springframework.context.annotation.Configuration;

@Configuration
// @ComponentScan(basePackages = { "de.openhabskill.service",
// "de.openhabskill.client" })
// @EnableJpaRepositories(basePackages = "de.openhabskill.entity")
// @EnableConfigurationProperties
public class ApplicationConfiguration {

	// @Bean
	// public DataSource dataSource() {
	// DriverManagerDataSource dataSource = new DriverManagerDataSource();
	// dataSource.setDriverClassName("${spring.datasource.driverClassName}");
	// dataSource.setUrl("${spring.datasource.url}");
	// dataSource.setUsername("${spring.datasource.username}");
	// dataSource.setPassword("${spring.datasource.password}");
	// return dataSource;
	// }
	//
	// @Bean
	// public EntityManager entityManager() {
	// return entityManagerFactory().getObject().createEntityManager();
	// }
	//
	// @Bean
	// public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
	// LocalContainerEntityManagerFactoryBean em = new
	// LocalContainerEntityManagerFactoryBean();
	// em.setDataSource(dataSource());
	// em.setPackagesToScan("de.openhabskill.entity");
	// return em;
	// }
}
