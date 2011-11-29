package net.ftlines.blog.cdidemo.model;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@ApplicationScoped
public class EmployeeRepository {

	@Inject
	private EntityManager em;

	public List<Employee> list(EmployeeCriteria criteria, int first, int max) {
		return query("FROM Employee", criteria, "ORDER BY lastName, firstName").setFirstResult(first)
				.setMaxResults(max).getResultList();
	}

	public int count(EmployeeCriteria criteria) {
		return ((Long) query("SELECT COUNT(*) FROM Employee", criteria, "").getSingleResult()).intValue();
	}

	private Query query(String base, EmployeeCriteria criteria, String suffix) {
		List<Object> params = new ArrayList<Object>();
		String hql = base + " WHERE 1=1";
		if (criteria.getHireDateMin() != null) {
			hql += "AND hireDate>=?";
			params.add(criteria.getHireDateMin());
		}
		if (criteria.getHireDateMax() != null) {
			hql += "AND hireDate<=?";
			params.add(criteria.getHireDateMax());
		}
		if (criteria.getTeam() != null) {
			hql += "AND team=?";
			params.add(criteria.getTeam());
		}

		Query query = em.createQuery(hql + suffix);
		for (int i = 0; i < params.size(); i++) {
			query.setParameter(i + 1, params.get(i));
		}
		return query;
	}
}
