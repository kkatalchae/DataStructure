package queue;

import java.util.NoSuchElementException;

public class LinkedLIstQueue<E> implements QueueInterface<E> {

    // Queue 구현에 쓰이는 Node
    private class Node<E> {
        E data;
        Node<E> next;

        Node(E data) {
            this.data = data;
            this.next = null;
        }
    }

    // 구현에 쓰이는 필드
    private Node<E> head;
    private Node<E> tail;
    private int size;

    // 생성자
    public LinkedLIstQueue() {
        this.head = null;
        this.tail = null;
        size = 0;
    }

    // Queue 에 데이터를 넣는 offer 메소드
    @Override
    public boolean offer(E value) {

        // 입력받은 데이터를 통해 새로운 노드를 만들어준다.
        Node<E> newNode = new Node<>(value);

        // 비어있는 큐의 경우 head 가 새로운 노드
        if (size == 0) head = newNode;

        // 비어있지 않은 큐의 경우, 기존 큐의 tail 의 다음에 새로운 노드를 연결해준다.
        if (size != 0) tail.next = newNode;

        // 새로 추가된 노드는 가장 마지막에 위치하기 때문에 tail 포인터를 새로운 노드로 교체 후 크기 증가
        tail = newNode;
        size++;

        return true;
    }

    // 가장 먼저 추가된 데이터를 삭제하는 poll 메소드
    @Override
    public E poll() {

        // 비어있는 큐의 경우 null 을 리턴
        if (size == 0) return null;

        // 임시 변수에 삭제될 데이터를 담아둔다.
        E deleted = head.data;

        // head 에 위치한 데이터를 삭제하면 head 가 될 노드를 가져온다.
        Node<E> nextNode = head.next;

        // 삭제될 데이터를 삭제
        head.data = null;
        head.next = null;

        // head 가 삭제된 노드의 다음 노드를 가리키도록 설정 후, 크기 감소
        head = nextNode;
        size--;

        return deleted;
    }

    // 데이터의 삭제는 poll 메소드와 동일하지만 삭제될 데이터가 없는 경우, 예외를 발생시키는 remove 메소드
    public E remove() {

        E removed = poll();

        if (removed == null) throw new NoSuchElementException();

        return removed;
    }

    // 가장 먼저 들어간 데이터, head 에 위치한 데이터를 확인하는 peek 메소드
    @Override
    public E peek() {

        if (size == 0) return null;

        return head.data;
    }

    // peek 메소드를 활용하지만 데이터가 없을 시 예외를 발생시키는 element 메소드
    public E element() {

        E element = peek();

        if (element == null) throw new NoSuchElementException();

        return element;
    }

    // 이하 설명 생략 
    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean contains(Object value) {

        for (Node<E> x = head; x != null; x = x.next) {
            if (value.equals(x.data)) return true;
        }

        return false;
    }

    public void clear() {

        for (Node<E> x = head; x != null; ) {
            Node<E> next = x.next;
            x.data = null;
            x.next = null;
            x = next;
        }

        size = 0;
        head = tail = null;
    }
}
