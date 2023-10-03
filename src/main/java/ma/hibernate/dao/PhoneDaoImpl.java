package ma.hibernate.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.Map;
import ma.hibernate.model.Phone;
import org.hibernate.SessionFactory;

public class PhoneDaoImpl extends AbstractDao implements PhoneDao {
    public PhoneDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Phone create(Phone phone) {
        factory.inTransaction(session -> session.persist(phone));
        return phone;
    }

    @Override
    public List<Phone> findAll(Map<String, String[]> params) {
        return factory.fromSession(session -> {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Phone> query = cb.createQuery(Phone.class);
            Root<Phone> phoneRoot = query.from(Phone.class);

            Predicate[] predicates = params.entrySet().stream()
                    .map(e -> phoneRoot.get(e.getKey()).in(e.getValue()))
                    .toArray(Predicate[]::new);
            query.where(predicates);
            return session.createQuery(query).getResultList();
        });
    }
}
