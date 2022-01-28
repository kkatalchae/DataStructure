package queue;

import java.util.NoSuchElementException;

public class ArrayQueue < E > implements QueueInterface < E > {

    /*
        # 배열로 구현한 Queue

        일반적으로 큐는 LinkedList 로 구현한 큐를 사용한다.

        하지만 ArrayDeque 나 PriorityQueue 처럼 내부적으로 배열을 사용한 구조도 있기에 공부를 위해 배열을 사용하여 구현하고자 한다.

        일반적인 배열을 사용하면 데이터를 추가, 삭제하면서 데이터들이 뒤로 쏠리는 현상이 발생한다.

        이 현상을 해결하고자 데이터 조작을 할 때마다 원소들을 재배치하는 것은 굉장히 비효율적이다.

        때문에 처음과 끝을 가리키는 포인터를 통해 원처럼 큐를 구성해준다. 이러한 구조를 '원형 큐' 라고 한다.
     */

    private static final int DEFAULT_CAPACITY = 64; // 기본 크기

    private Object[] array; // Queue 를 구성할 배열
    private int size; // Queue 에 담겨있는 데이터 개수

    private int front; // Queue 의 맨 처음
    private int rear; // Queue 의 맨 끝

    public ArrayQueue() { // 기본 생성자
        this.array = new Object[DEFAULT_CAPACITY];
        this.size = 0;
        this.front = 0;
        this.rear = 0;
    }

    public ArrayQueue(int capacity) { // 입력받은 크기로 배열을 생성하는 생성자
        this.array = new Object[capacity];
        this.size = 0;
        this.front = 0;
        this.rear = 0;
    }

    // 동적 재할당을 위한 resize 메소드
    private void resize(int newCapacity) {
        int arrayCapacity = array.length;

        Object[] newArray = new Object[newCapacity];

        // Queue 의 front 는 비워놓고 시작하여 front == rear 가 되어있을 때 Queue 비어있는지 확인할 수 있도록 한다.
        for (int i = 1, j = front + 1; i <= size; i++, j++) {
            newArray[i] = array[j % arrayCapacity];
        }

        this.array = newArray; // 기존 배열을 새롭게 만들어진 배열로 대체

        front = 0;
        rear = size;
    }

    // Queue 에 입력받은 데이터를 추가하는 offer 메소드, 배열의 가장 마지막 rear 에 추가한다.
    @Override
    public boolean offer(E item) {

        // 가득 차 있을 경우 크기를 재할당
        if ((rear + 1) % array.length == front) {
            // 만약 rear + 1 이 배열의 마지막일 경우, 다시 처음부터 순환해야 하므로 배열의 크기로 나눈 나머지로 비교
            resize(array.length * 2);
        }

        rear = (rear + 1) % array.length;

        array[rear] = item;
        size++;

        return true;
    }

    // Queue 의 front 즉, 가장 먼저 추가된 데이터를 삭제하는 poll 메소드
    @SuppressWarnings("unchecked")
    @Override
    public E poll() {

        if (size == 0) {
            return null;
        }

        front = (front + 1) % array.length;

        E item = (E) array[front];

        array[front] = null;
        size--;

        if (array.length > DEFAULT_CAPACITY && size < (array.length / 4)) {
            resize(Math.max(DEFAULT_CAPACITY, array.length / 2));
        }

        return item;
    }

    // poll 메소드와 동일한 기능이며 예외를 발생시키는 remove 메소드
    public E remove() {
        E item = poll();

        if (item == null) throw new NoSuchElementException();

        return item;
    }

    // Queue 에 가장 먼저 추가된 데이터 즉, front 에 위치한 데이터를 확인하고 제거하지 않는 peek 메소드
    @SuppressWarnings("unchecked")
    @Override
    public E peek() {

        if (size == 0) return null;

        E item = (E) array[(front + 1) % array.length];
        return item;
    }

    // peek 메소드와 기능은 동일하며 비어있을 시 예외를 발생시키는 element 메소드
    public E element() {
        E item = peek();

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
            if (array[idx].equals(value)) return true;
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
