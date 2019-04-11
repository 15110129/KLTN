package com.kltn.motelservice.repository;

import com.kltn.motelservice.entity.Post;
import com.kltn.motelservice.model.SearchForm;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class PostSpecification implements Specification<Post> {
    private SearchForm searchForm;

    public PostSpecification(SearchForm searchForm) {
        this.searchForm = searchForm;
    }

    @Override
    public Predicate toPredicate(Root<Post> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
        Predicate p = cb.disjunction();

        //Search by motel and district
        if (searchForm.getMotel() == 1 && searchForm.getIdDistrict() != 0) {
            p.getExpressions().add(
                    cb.and(cb.greaterThanOrEqualTo(root.get("accomodation").get("acreage"), searchForm.getAcreageStart()),
                            cb.lessThanOrEqualTo(root.get("accomodation").get("acreage"), searchForm.getAcreageEnd()),
                            cb.greaterThanOrEqualTo(root.get("accomodation").get("price"), searchForm.getPriceStart()),
                            cb.lessThanOrEqualTo(root.get("accomodation").get("price"), searchForm.getPriceEnd()),
                            cb.equal(root.get("accomodation").get("motel"), true),
                            cb.equal(root.get("accomodation").get("district").get("id"), searchForm.getIdDistrict())));
        }

        if (searchForm.getMotel() == 2 && searchForm.getIdDistrict() != 0) {
            p.getExpressions().add(
                    cb.and(cb.greaterThanOrEqualTo(root.get("accomodation").get("acreage"), searchForm.getAcreageStart()),
                            cb.lessThanOrEqualTo(root.get("accomodation").get("acreage"), searchForm.getAcreageEnd()),
                            cb.greaterThanOrEqualTo(root.get("accomodation").get("price"), searchForm.getPriceStart()),
                            cb.lessThanOrEqualTo(root.get("accomodation").get("price"), searchForm.getPriceEnd()),
                            cb.equal(root.get("accomodation").get("motel"), false),
                            cb.equal(root.get("accomodation").get("district").get("id"), searchForm.getIdDistrict())));
        }
        //Search by motel and district

        //Search by motel
        if (searchForm.getMotel() == 1 && searchForm.getIdDistrict() == 0) {
            p.getExpressions().add(
                    cb.and(cb.greaterThanOrEqualTo(root.get("accomodation").get("acreage"), searchForm.getAcreageStart()),
                            cb.lessThanOrEqualTo(root.get("accomodation").get("acreage"), searchForm.getAcreageEnd()),
                            cb.greaterThanOrEqualTo(root.get("accomodation").get("price"), searchForm.getPriceStart()),
                            cb.lessThanOrEqualTo(root.get("accomodation").get("price"), searchForm.getPriceEnd()),
                            cb.equal(root.get("accomodation").get("motel"), true)));
        }

        if (searchForm.getMotel() == 2 && searchForm.getIdDistrict() == 0) {
            p.getExpressions().add(
                    cb.and(cb.greaterThanOrEqualTo(root.get("accomodation").get("acreage"), searchForm.getAcreageStart()),
                            cb.lessThanOrEqualTo(root.get("accomodation").get("acreage"), searchForm.getAcreageEnd()),
                            cb.greaterThanOrEqualTo(root.get("accomodation").get("price"), searchForm.getPriceStart()),
                            cb.lessThanOrEqualTo(root.get("accomodation").get("price"), searchForm.getPriceEnd()),
                            cb.equal(root.get("accomodation").get("motel"), false)));
        }
        //Search by motel

        //Search by district
        if (searchForm.getMotel() == 0 && searchForm.getIdDistrict() != 0) {
            p.getExpressions().add(
                    cb.and(cb.greaterThanOrEqualTo(root.get("accomodation").get("acreage"), searchForm.getAcreageStart()),
                            cb.lessThanOrEqualTo(root.get("accomodation").get("acreage"), searchForm.getAcreageEnd()),
                            cb.greaterThanOrEqualTo(root.get("accomodation").get("price"), searchForm.getPriceStart()),
                            cb.lessThanOrEqualTo(root.get("accomodation").get("price"), searchForm.getPriceEnd()),
                            cb.equal(root.get("accomodation").get("district").get("id"), searchForm.getIdDistrict())));
        }
        //Search by district

        //Search without motel and district
        if (searchForm.getMotel() == 0 && searchForm.getIdDistrict() == 0) {
            p.getExpressions().add(
                    cb.and(cb.greaterThanOrEqualTo(root.get("accomodation").get("acreage"), searchForm.getAcreageStart()),
                            cb.lessThanOrEqualTo(root.get("accomodation").get("acreage"), searchForm.getAcreageEnd()),
                            cb.greaterThanOrEqualTo(root.get("accomodation").get("price"), searchForm.getPriceStart()),
                            cb.lessThanOrEqualTo(root.get("accomodation").get("price"), searchForm.getPriceEnd())));
        }
        //Search without motel and district

//        p.getExpressions().add(
//                cb.and(cb.greaterThanOrEqualTo(root.get("accomodation").get("price"), searchForm.getPriceStart()),
//                        cb.lessThanOrEqualTo(root.get("accomodation").get("price"), searchForm.getPriceEnd())));

//        if (searchForm.getMotel() != 2 && searchForm.getMotel() == 1) {
//            p.getExpressions()
//                    .add(cb.equal(root.get("accomodation").get("motel"), true));
//        }
//
//        if (searchForm.getMotel() != 2 && searchForm.getMotel() == 0) {
//            p.getExpressions()
//                    .add(cb.equal(root.get("accomodation").get("motel"), false));
//        }
//
//        if (searchForm.getIdDistrict() != 0) {
//            p.getExpressions()
//                    .add(cb.equal(root.get("accomodation").get("district").get("id"), searchForm.getIdDistrict()));
//        }

        return p;
    }
}
