package queue;

import java.util.NoSuchElementException;

public class ArrayDeque < E> implements QueueInterface < E > {

    private static final int DEFAULT_CAPACITY = 64; // 기본 자료구조 크기

    private Object[] array; // 덱 구현에 사용되는 배열
    private int size; // 데이터의 갯수

    private int front; // 자료구조의 처음을 나타내는 포인터
    private int rear; // 자료구조의 끝은 나타내는 포인터

    public ArrayDeque() { // 생성자
        this.array = new Object[DEFAULT_CAPACITY];
        this.size = 0;
        this.front = 0;
        this.rear = 0;
    }

    public ArrayDeque(int capacity) { // 입력받은 크기로 덱을 생성하는 생성자
        this.array = new Object[capacity];
        this.size = 0;
        this.front = 0;
        this.rear = 0;
    }

    // 동적 할당을 위한 resize 메소드
    private void resize(int newCapacity) {

        int arrayCapacity = array.length;

        Object[] newArray = new Object[newCapacity];

        for (int i = 1, j = front + 1; i <= size; i++, j++) {
            newArray[i] = array[j % arrayCapacity];
        }

        this.array = null;
        this.array = newArray;

        front = 0;
        rear = size;
    }

    // offer 메소드는 기존 자료 구조의 끝 부분에 데이터를 추가하는 메소드
    @Override
    public boolean offer(E item) {
        return offerLast(item);
    }

    public boolean offerLast(E item) {

        // 큐를 배열로 구현했을 때와 마찬가지로 원형 구조로 구현되기 때문에 길이로 나눠 비교함으로써 가득차 있는지 확인
        if ((rear + 1) % array.length == front) {
            resize(array.length * 2);
        }

        // rear 포인터를 한 칸 미룬 뒤, 입력받은 데이터를 rear 에 할당
        rear = (rear + 1) % array.length;
        array[rear] = item;
        size++;

        return true;
    }

    // 덱 ( Deque ) 은 양 방향으로 데이터를 조작할 수 있기 때문에 맨 앞에 추가하는 offerFirst 메소드를 구현하고 있다.
    public boolean offerFirst(E item) {

        if ((front - 1 + array.length) % array.length == rear) { // 자료 구조가 가득차있을 경우
            resize(array.length * 2);
        }

        array[front] = item;
        front = (front - 1 + array.length) % array.length;
        size++;

        return true;
    }

    // 덱 ( Deque ) 에 있는 데이터를 제거하는 poll 메소드, 기본적인 poll 메소드는 front 에 위치한 데이터를 제거한다.
    @Override
    public E poll() {
        return pollFirst();
    }

    public E pollFirst() {

        if (size == 0) return null;

        // 큐에서 구현했듯이 front 는 비워두고 다음 인덱스부터 데이터를 채워넣기 때문에 front 를 한 칸 뒤로 옮겨준다.
        front = (front + 1) % array.length;

        // 데이터를 삭제하기에 앞서 임시 변수에 담아주고 데이터를 삭제, 크기를 줄여준다.
        @SuppressWarnings("unchecked")
        E item = ( E ) array[front];
        array[front] = null;
        size--;

        // 기본 크기를 넘지만 크기에 비해 데이터가 현저히 ( 25 % ) 부족한 경우, 크기를 반으로 줄여준다.
        if (array.length > DEFAULT_CAPACITY && size < (array.length / 4)) {
            // 크기를 줄여주되 기본 크기 이하로는 떨어지지 않도록 설정
            resize(Math.max(DEFAULT_CAPACITY, array.length / 2));
        }

        return item;
    }

    public E pollLast() {

        if (size == 0) return null;

        @SuppressWarnings("unchecked")
        E item = ( E ) array[rear];

        array[rear] = null;
        rear = (rear - 1 + array.length) % array.length;
        size--;

        if (array.length > DEFAULT_CAPACITY && size < (array.length / 4)) {
            resize(Math.max(DEFAULT_CAPACITY, array.length / 2));
        }

        return item;
    }

    // remove 메소드는 poll 메소드를 사용해서 구현하고 null 이 리턴될 경우 ( 비어있을 경우 ) 예외를 발생시킨다.
    public E remove() {
        return removeFirst();
    }

    public E removeFirst() {
        E item = pollFirst();

        if (item == null) throw new NoSuchElementException();

        return item;
    }

    public E removeLast() {

        E item = pollLast();

        if (item == null) throw new NoSuchElementException();

        return item;
    }

    // peek 메소드는 자료구조의 맨 앞이나 뒤에 위치한 데이터를 확인할 때 쓰이는 메소드이다.
    @Override
    public E peek() {
        return peekFirst();
    }

    public E peekFirst() {

        if (size == 0) return null;

        @SuppressWarnings("unchecked")
        E item = ( E ) array[(front + 1) % array.length];

        return item;
    }

    public E peekLast() {

        if (size == 0) return null;

        @SuppressWarnings("unchecked")
        E item = ( E ) array[rear];

        return item;
    }

    // element, getFirst, getLast 는 peek 메소드와 기능은 같고 비어있는 경우 예외를 발생시킨다는 차이가 있다.
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

    // 이하 설명 생략
    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean contains(Object value) {

        int start = (front + 1) % array.length;

        for (int i = 0, idx = start; i < size; i++, idx = (idx + 1) % array.length) {
            if (array[idx].equals(value)) {
                return true;
            }
        }

        return false;
    }

    public void clear() {
        for (int i = 0; i < array.length; i++) {
            array[i] = null;
        }

        front = rear = size = 0;
    }
}
