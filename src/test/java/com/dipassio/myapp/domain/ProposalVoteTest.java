package com.dipassio.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.dipassio.myapp.web.rest.TestUtil;

public class ProposalVoteTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProposalVote.class);
        ProposalVote proposalVote1 = new ProposalVote();
        proposalVote1.setId(1L);
        ProposalVote proposalVote2 = new ProposalVote();
        proposalVote2.setId(proposalVote1.getId());
        assertThat(proposalVote1).isEqualTo(proposalVote2);
        proposalVote2.setId(2L);
        assertThat(proposalVote1).isNotEqualTo(proposalVote2);
        proposalVote1.setId(null);
        assertThat(proposalVote1).isNotEqualTo(proposalVote2);
    }
}
