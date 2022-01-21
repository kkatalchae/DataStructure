package list;

import java.util.NoSuchElementException;

/*
    # LinkedList

    List 인터페이스를 상속하는 클래스

    기본적으로 List 인터페이스를 상속하고 있기 때문에 동적 할당이나 연속적인 데이터만 허용한다는 점은 ArrayList 와 동일하다.

    ArrayList 와 차이점이라고 한다면 LinkedList 는 배열이 아니라 Node 라는 객체를 이용해서 구현하고 있다는 점이다.

    Node 는 데이터와 함께 주소 데이터를 가지고 있는 객체이며 이러한 Node 가 연결되어 있는 자료 구조가 LinkedList 다.

    LinkedList 는 연결되어 있는 방식에 따라 단일 연결 리스트, 이중 연결 리스트, 원형 연결 리스트로 구분할 수 있다.

    LinkedList 는 ArrayList 에서 설명했다시피 데이터의 검색보다 데이터의 추가 / 삭제가 빈번할 때 사용하는 것이 효율적이다.


 */

// 단일 연결 리스트
public class SingleLinkedList<E> implements List<E> {

    private class Node<E> { // 노드 객체
        E data;
        Node<E> next; // 다음 노드를 가리키는 참조 변수

        Node(E data) { // 생성자
            this.data = data;
            this.next = null;
        }
    }

    private Node<E> head; // 리스트의 첫 노드
    private Node<E> tail; // 리스트의 마지막 노드드
    private int size; // 리스트의 크기

    public SingleLinkedList() { // 생성자
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    // 특정 위치의 노드를 반환하는 메소드
    private Node<E> search(int index) {

        // 리스트의 범위를 벗어나면 예외를 발생시킨다.
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        // 새로운 지역 변수에 리스트의 첫 노드를 대입
        Node<E> node = head;

        /*
        LinkedList는 ArrayList 처럼 인덱스 값으로 찾지 못하기 때문에
        첫 노드부터 시작해서 next 포인터가 가리키고 있는 노드를 찾아가면서 원하는 위치의 값이 무엇인지 찾아야 한다.
         */
        for (int i = 0; i < index; i++) {
            node = node.next;
        }
        return node;
    }

    // 입력받은 값을 리스트에 맨 처음에 위치시키는 addFirst 메소드
    public void addFirst(E value) {

        // 입력받은 값을 이용해 새로운 노드 생성
        Node<E> newNode = new Node<>(value);

        newNode.next = head; // 새로운 노드의 next 포인터를 기존 리스트의 첫 노드와 연결
        head = newNode; // 리스트의 처음을 가리키는 head 가 새로운 노드를 가리키도록 변경
        size++; // 새로운 노드가 추가되었으니 리스트의 크기를 증가

        // 빈 리스트였을 경우 새로운 노드가 처음이자 마지막이기 때문에 tail 포인터도 새로운 노드를 가리키도록 함.
        if (head.next == null) {
            tail = head;
        }

    }

    // 입력받은 값을 리스트의 마지막에 추가하는 addLast 메소드
    public void addLast(E value) {

        // 새로운 노드 생성
        Node<E> newNode = new Node<>(value);

        // 빈 리스트일 경우 맨 앞에 추가하는 것과 같기 때문에 기존 코드 재활용
        if (size == 0) {
            addFirst(value);
            return; // 뒤에 있는 코드가 실행되지 않도록 return 문 작성
        }

        tail.next = newNode; // 리스트의 마지막을 가리키는 tail 노드의 next 포인트가 새로운 노드를 가리키도록 함.
        tail = newNode; // tail 노드를 새로운 노드로 교체
        size++; // 사이즈 증가

    }

    // 자바에서 기본적으로 구현하고 있는 add 메소드는 리스트에 마지막에 노드를 추가하는 메소드드
    @Override
    public boolean add(E value) {
        addLast(value);
        return true;
    }

    // 입력받은 위치에 값을 추가 하는 add 메소드
    @Override
    public void add(int index, E value) {

        // 리스트의 범위를 벗어나는 경우 예외를 발생시킨다.
        if (index > size || index < 0) {
            throw new IndexOutOfBoundsException();
        }

        // 원하는 위치가 처음이나 마지막일 경우, 인덱스를 찾을 필요가 없기 때문에 기존의 코드를 재활용하여 처리
        if (index == 0) {
            addFirst(value);
            return;
        }

        if (index == size) {
            addLast(value);
            return;
        }


        Node<E> prevNode = search(index - 1); // 추가하려는 위치 이전 노드
        Node<E> nextNode = prevNode.next; // 추가하려는 위치의 노드
        Node<E> newNode = new Node<>(value); // 추가하려는 노드

        prevNode.next = newNode; // 추가하려는 위치 이전 노드의 next 포인터가 새로운 노드를 가리키도록 연결
        newNode.next = nextNode; // 새로운 노드의 next 포인터가 추가하려는 위치에 있는 기존 노드와 연결
        size++; // 크기 증가

    }


    // 리스트의 첫 노드를 없애는 remove 메소드
    public E remove() {

        // 리스트의 첫 노드
        Node<E> headNode = head;

        // 리스트가 첫 노드가 null 즉, 비어있는 리스트의 경우 예외가 발생하도록 코드 작성
        if (headNode == null) throw new NoSuchElementException();

        // 빈 리스트가 아닐 경우 첫 노드의 데이터를 변수에 담아준다.
        E removed = headNode.data;

        // 없앨 노드의 다음 노드를 변수에 담아준다.
        Node<E> nextNode = head.next;

        // 첫 노드의 데이터와 next 포인터를 null 로 없애준다.
        head.data = null;
        head.next = null;

        // 리스트의 첫 노드를 담아두었던 nextNode로 교체 후, 크기를 줄여준다.
        head = nextNode;
        size--;

        // 리스트에 하나만 있었을 때는 head 와 tail 이 같은 곳을 가리키고 있기 때문에 tail 도 null 로 바꿔준다.
        if (size == 0) tail = null;

        return removed;
    }

    // 원하는 위치에 있는 노드를 없애기 위한 remove 메소드
    @Override
    public E remove(int index) {

        // 범위를 벗어나는 경우 예외 발생
        if (index >= size || index < 0) throw new IndexOutOfBoundsException();

        if (index == 0) return remove(); // 첫 노드를 삭제하고자 할 경우 기존 코드 재활용

        Node<E> prevNode = search(index - 1); // 삭제할 위치 이전의 노드
        Node<E> removeNode = prevNode.next; // 삭제할 위치의 노드
        Node<E> nextNode = removeNode.next; // 삭제할 위치 다음 노드

        E removed = removeNode.data; // 삭제할 노드의 데이터를 리턴하기 위해 임시 변수에 담아준다.

        prevNode.next = nextNode; // 이전 노드의 next 포인터를 삭제할 노드에서 다음 노드로 바꿔준다.

        // 데이터 삭제
        removeNode.next = null;
        removeNode.data = null;
        size--;

        return removed;
    }


    // 리스트에서 원하는 값에 해당하는 노드를 제거하는 remove 메소드
    @Override
    public boolean remove(Object value) {

        Node<E> prevNode = head;
        Node<E> removeNode = head;

        // 리스트를 돌면서 입력받은 값을 찾아준다.
        for (; removeNode != null; removeNode = removeNode.next) {
            if (value.equals(removeNode.data)) {
                break;
            }
            prevNode = removeNode;
        }

        // 리스트에 입력받은 값이 없는 경우 false 리턴
        if (removeNode == null) return false;

        // 입력받은 값이 리스트에 맨 처음에 있을 경우는 기존에 작성한 코드를 재활용 처리
        if (removeNode.equals(head)) {
            remove();
            return true;
        } else {
            // 첫번째에 위치하지 않은 경우
            prevNode.next = removeNode.next; // 이전 노드의 next 포인터가 제거할 노드의 다음 노드를 가르키도록 수정

            // 데이터를 삭제한 후 크기를 줄여주고 true 리턴
            removeNode.data = null;
            removeNode.next = null;
            size--;
            return true;
        }
    }

    // 입력받은 위치의 데이터를 가져오는 get 메소드
    @Override
    public E get(int index) {
        return search(index).data;
    }

    // 입력받은 위치의 데이터를 수정하기 위한 set 메소드
    @Override
    public void set(int index, E value) {
        Node<E> replaceNode = search(index);
        replaceNode.data = value;
    }

    // 입력받은 데이터의 위치를 구해오는 indexOf 메소드 ( 단, 중복되는 값이 있을 경우 가장 먼저 나타나는 위치를 리턴 )
    @Override
    public int indexOf(Object value) {
        int index = 0;

        for (Node<E> node = head; node != null; node = node.next) {
            if (value.equals(node.data)) return index;
        }

        return -1;
    }

    // 리스트에 입력받은 값이 존재하는지 확인하는 contains 메소드
    @Override
    public boolean contains(Object value) {
        return indexOf(value) >= 0;
    }

    // 리스트의 크기를 구하는 size 메소드
    @Override
    public int size() {
        return size;
    }

    // 비어있는 리스트인지 확인하는 isEmpty 메소드
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // 리스트를 비우는 clear 메소드
    @Override
    public void clear() {
        for (Node<E> x = head; x != null;) {
            Node<E> nextNode = x.next;
            x.data = null;
            x.next = null;
            x = nextNode;
        }
        head = tail = null;
        size = 0;
    }
}
