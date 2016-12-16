package org.superbiz.moviefun;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class MoviesDbConfig {

    @Value("${pal.moviesDb.url}") String moviesDbUrl;
    @Value("${pal.moviesDb.username}") String moviesDbUsername;
    @Value("${pal.moviesDb.password}") String moviesDbPassword;

    @Bean
    @Qualifier("movies")
    public DataSource moviesDataSource() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(moviesDbUrl);
        dataSource.setUser(moviesDbUsername);
        dataSource.setPassword(moviesDbPassword);
        return dataSource;
    }

    @Bean
    HibernateJpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setDatabase(Database.MYSQL);
        jpaVendorAdapter.setDatabasePlatform("org.hibernate.dialect.MySQL5InnoDBDialect");
        jpaVendorAdapter.setGenerateDdl(true);
        return jpaVendorAdapter;
    }

    @Bean
    @Qualifier("movies")
    LocalContainerEntityManagerFactoryBean moviesEntityManagerFactoryBean(DataSource moviesDataSource, HibernateJpaVendorAdapter jpaVendorAdapter) {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();

        factoryBean.setDataSource(moviesDataSource);
        factoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        factoryBean.setPackagesToScan(getClass().getPackage().getName());
        factoryBean.setPersistenceUnitName("movies");

        return factoryBean;
    }

    @Bean
    @Qualifier("movies")
    PlatformTransactionManager moviesTransactionManager(@Qualifier("movies") LocalContainerEntityManagerFactoryBean factoryBean) {
        return new JpaTransactionManager(factoryBean.getObject());
    }
}
