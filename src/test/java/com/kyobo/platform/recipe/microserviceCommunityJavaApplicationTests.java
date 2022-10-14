package com.kyobo.platform.recipe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


//import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

//import com.kyobo.platform.recipe.answer.Answer;
//import com.kyobo.platform.recipe.question.Question;
//import com.kyobo.platform.recipe.question.QuestionRepository;

@SpringBootTest
class microserviceCommunityJavaApplicationTests {
	
//	@Autowired
//	private QuestionRepository questionRepository;
	
//	@Autowired
//	private AnswerRepository answerRepository;
	

//	@Test
//	void testJpa() {
//		Question q1 = new Question();
//        q1.setSubject("kyoboBoard가 무엇인가요?");
//        q1.setContent("kyoboBoard에 대해서 알고 싶습니다.");
//        q1.setCreateDate(LocalDateTime.now());
//        this.questionRepository.save(q1);  // 첫번째 질문 저장
//
//        Question q2 = new Question();
//        q2.setSubject("스프링부트 모델 질문입니다.");
//        q2.setContent("id는 자동으로 생성되나요?");
//        q2.setCreateDate(LocalDateTime.now());
//        this.questionRepository.save(q2);  // 두번째 질문 저장
//       
//	}
	
//	@Test
//	void testJpa() {
//		List<Question> all = this.questionRepository.findBySubjectLike("kyoboBoard%");
//		
//		Question q = all.get(0);
//		assertEquals("kyoboBoard가 무엇인가요?", q.getSubject());
//		Question q = this.questionRepository.findBySubject("kyoboBoard가 무엇인가요?");
//      Question q = this.questionRepository.findBySubjectAndContent("kyoboBoard가 무엇인가요?", "kyoboBoard에 대해서 알고 싶습니다.");
//		assertEquals(1, q.getId());
	
//	@Test
//	void testJpa() {
//		Optional<Question> oq = this.questionRepository.findById(1);
//		assertTrue(oq.isPresent());
//		Question q = oq.get();
//		q.setSubject("수정된 제목");
//		this.questionRepository.save(q);
	
//    @Test
//    void testJpa() {
//        assertEquals(2, this.questionRepository.count());
//        Optional<Question> oq = this.questionRepository.findById(1);
//        assertTrue(oq.isPresent());
//        Question q = oq.get();
//        this.questionRepository.delete(q);
//        assertEquals(1, this.questionRepository.count());
        
//    @Test
//    void testJpa() {
//        Optional<Question> oq = this.questionRepository.findById(2);
//        assertTrue(oq.isPresent());
//        Question q = oq.get();
//
//        Answer a = new Answer();
//        a.setContent("네 자동으로 생성됩니다.");
//        a.setQuestion(q);  // 어떤 질문의 답변인지 알기위해서 Question 객체가 필요하다.
//        a.setCreateDate(LocalDateTime.now());
//        this.answerRepository.save(a); 
	
//    @Test
//    void testJpa() {
//        Optional<Answer> oa = this.answerRepository.findById(1);
//        assertTrue(oa.isPresent());
//        Answer a = oa.get();
//        assertEquals(2, a.getQuestion().getId());
	
//	@Transactional
//    @Test
//    void testJpa() {
//        Optional<Question> oq = this.questionRepository.findById(2);
//        assertTrue(oq.isPresent());
//        Question q = oq.get();
//
//        List<Answer> answerList = q.getAnswerList();
//
//        assertEquals(1, answerList.size());
//        assertEquals("네 자동으로 생성됩니다.", answerList.get(0).getContent());	
//
//		
//	
//	}
	
}
