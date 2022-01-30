package queue;

import java.util.NoSuchElementException;

public class LinkedListDeque < E > implements QueueInterface < E > {

    /*
        # 덱 ( Deque, Double-ended Queue )

        큐의 양쪽으로 데이터의 삽입과 삭제를 수행할 수 있는 자료구조를 말한다.

        덱은 어떤 쪽으로 입력하고 어떤 쪽으로 출력하느냐에 따라서 스택 ( Stack ) 으로 활용할 수 있고, 큐 ( Queue ) 로 활용할 수 있다.

        한쪽으로만 입력 가능하도록 설정한 덱을 스크롤 ( scroll ) 이라고 하며, 한쪽으로만 출력 가능한 덱을 셸프 ( shelf ) 라고 한다.

        자료구조의 시작과 끝에 위치한 데이터를 조작하고자 할 때 유용하게 사용되는 자료구조이다. 
    */

    // Deque 를 구현하는 데 사용하는 노드는 이중 연결 리스트를 구현할 때 모습과 동일하다.
    private class Node < E > {
        E data;
        Node<E> next;
        Node<E> prev;

        Node(E data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }

    private Node<E> head; // 맨 처음을 가리키는 head 포인터
    private Node<E> tail; // 마지막 노드를 가리키는 tail 포인터
    private int size; // 데이터의 갯수

    public LinkedListDeque() { // 기본 생성자
        head = null;
        tail = null;
        size = 0;
    }

    // 데이터를 자료구조에 추가하는 offer 메소드
    @Override
    public boolean offer(E value) {
        return offerLast(value);
    }

    public boolean offerFirst(E value) {

        // 입력받은 데이터로 새로운 노드 생성
        Node<E> newNode = new Node<>(value);

        // 새로운 노드와 기존 head 노드를 연결
        newNode.next = head;

        // 두 개 이상의 노드가 연결되어 있는 경우, 기존 head 의 prev 가 새로운 노드를 가리키도록 설정
        if (head != null) head.prev = newNode;

        head = newNode;
        size++;

        // 기존에 비어있던 Deque 일 경우 head 와 tail 이 같도록 조정
        if (head.next == null) tail = head;

        return true;
    }

    public boolean offerLast(E value) {

        // 비어있는 Deque 일 경우 offerFirst 메소드와 같기 때문에 기존 메소드 활용
        if (size == 0) return offerFirst(value);

        Node<E> newNode = new Node<>(value);

        // tail 노드와 새로운 노드를 연결해준 후, tail 포인터가 새로운 노드를 가리키도록 변경
        tail.next = newNode;
        newNode.prev = tail;
        tail = newNode;
        size++;

        return true;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    public E pollFirst() {

        if (size == 0) return null;

        E element = head.data;

        // 그냥 삭제하면 가장 앞에 있는 노드에 접근할 수 없기 때문에 head 다음 노드를 변수로 담아준다.
        Node<E> nextNode = head.next;

        // 데이터 삭제
        head.data = null;
        head.next = null;

        // 기존 Deque 에 두 개 이상의 노드가 있었을 경우, nextNode 와 기존 head 를 연결하던 것을 끊어준다.
        if (nextNode != null) nextNode.prev = null;

        head = null;
        head = nextNode;
        size--;

        // 삭제한 후 빈 Deque 가 될 경우 tail 포인터도 null 로 초기화
        if (size == 0) tail = null;

        return element;
    }

    // poll 과 remove , peek 와 element 등의 차이는 ArrayDeque 에서 설명하고 있기 때문에 생략
    public E remove() {
        return removeFirst();
    }

    public E removeFirst() {
        E element = poll();

        if (element == null) throw new NoSuchElementException();

        return element;
    }

    public E pollLast() {

        if (size == 0) return null;

        E element = tail.data;

        Node<E> prevNode = tail.prev;

        tail.data = null;
        tail.prev = null;

        if (prevNode != null) prevNode.next = null;

        tail = null;
        tail = prevNode;
        size--;

        if (size == 0) head = null;

        return element;
    }

    public E removeLast() {
        E element = pollLast();

        if (element == null) throw new NoSuchElementException();

        return element;
    }

    // 이하 설명 생략
    @Override
    public E peek() {
        return peekFirst();
    }

    public E peekFirst() {

        if (size == 0) return null;

        return head.data;
    }

    public E peekLast() {

        if (size == 0) return null;

        return tail.data;
    }

    public E element() {
        return getFirst();
    }

    public E getFirst() {

        E item = peek();

        if (item == null) throw new NoSuchElementException();

        return item;
    }

    public E getLast() {

        E item = peekLast();

        if (item == null) throw new NoSuchElementException();

        return item;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean contains(Object value) {

        for (Node<E> x = head; x != null; x = x.next) {
            if (value.equals(x.data)) {
                return true;
            }
        }
        return false;
    }

    public void clear() {
        for (Node<E> x = head; x != null;) {

            Node<E> next = x.next;

            x.data = null;
            x.next = null;
            x.prev = null;
            x = next;
        }

        size = 0;
        head = tail = null;
    }
}
