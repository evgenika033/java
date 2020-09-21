package coupon.core.service;

import java.util.List;

public interface IService<T> {
	void add(T addObject);

	T get(Integer id);

	List<T> get();

	void update(T updateObject);

	void delete(Integer id);
}
