package org.mybatis.jpetstore.repository;

import org.mybatis.jpetstore.domain.Sequence;
import org.mybatis.jpetstore.mapper.SequenceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 시퀀스 값을 관리하는 레포지터리입니다.
 */
@Repository
public class SequenceRepository {

    private final SequenceMapper mapper;

    @Autowired
    public SequenceRepository(SequenceMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 시퀀스 정보를 조회합니다.
     *
     * @param sequence 조회할 시퀀스 키
     * @return 시퀀스 정보
     */
    public Sequence getSequence(Sequence sequence) {
        return mapper.getSequence(sequence);
    }

    /**
     * 시퀀스 값을 업데이트합니다.
     *
     * @param sequence 업데이트할 시퀀스
     */
    public void updateSequence(Sequence sequence) {
        mapper.updateSequence(sequence);
    }
}
