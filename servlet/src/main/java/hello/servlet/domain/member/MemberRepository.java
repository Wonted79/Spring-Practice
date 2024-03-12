package hello.servlet.domain.member;

import javax.print.attribute.Attribute;
import java.util.*;

/**
   * 동시성 고려를 위해서는 ConcurrentHashMap , AtomicLong 사용고려
 */
public class MemberRepository {

    private Map<Long , Member> store = new HashMap<>();
    private static long sequence = 0L;

    private static final MemberRepository instance = new MemberRepository();

    public static MemberRepository getInstance(){
        return  instance;
    }
    private  MemberRepository() {

    }

    public Member save(Member member){
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;
    }

    public  Member findById (Long id ){
        return  store.get(id);
    }

    public List<Member> findAll() {
        return  new ArrayList<>(store.values());
    }

    public void clearStore() {
        store.clear();
    }

}
