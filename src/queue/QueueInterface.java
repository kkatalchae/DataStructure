package queue;

public interface QueueInterface < E > {

   /*
        자바 Queue 인터페이스 ( SE 16 & JDK 16 API 참고 )

        Collection, Iterable 을 상속하고 있으며 ArrayDeque, LinkedList, PriorityQueue 등에 의해 구현된다.

        First In First Out 먼저 들어온 것이 먼저 나가는 선입선출을 특징으로 한다.

        시간 순으로 어떤 작업 또는 데이터를 처리할 필요가 있는 것들을 큐를 통해 처리한다고 이해하면 편하다.

        알고리즘에서는 BFS ( 너비 우선 탐색 ) 에 사용된다.
    */


    boolean offer(E value);
    /*
        Queue 에 데이터를 넣을 때 사용되는 메소드, 자바 API 문서에서는 add 메소드도 인테페이스에 명시해놓았으나

        비슷한 기능을 담당하기 때문에 offer 만 구현하도록 하려고 한다.
     */

    E peek();

    /*
        Queue 자료구조에서 가장 먼저 나오는 데이터가 무엇인지 확인하는 메소드

        마찬가지로 element 메소드와 비슷하기 때문에 peek 메소드만 구현
     */

    E poll();

    /*
        Queue 자료구조에서 가장 먼저 나오는 데이터를 삭제하고 리턴하는 메소드

        마찬가지로 remove 메소드는 구현하지 않음
     */


}
