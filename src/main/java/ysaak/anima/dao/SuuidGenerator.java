package ysaak.anima.dao;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import ysaak.anima.utils.Id;

import java.io.Serializable;

public class SuuidGenerator implements IdentifierGenerator {
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        return Id.generate();
    }

    @Override
    public boolean supportsJdbcBatchInserts() {
        return true;
    }
}
