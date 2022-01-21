package list;

public class DoublyLinkedList {

    // 이중 연결 리스트에서 쓰이는 노드 객체 ( ArrayList 의 노드에서 이전 노드를 가리키는 포인터 추가 )
    private class Node{
        private Object data;
        private Node next;
        private Node prev;

        public Node(Object input) {
            this.data = input;
            this.next = null;
            this.prev = null;
        }
    }

    // 이중 연결 리스트에서 사용되는 필드 ( 멤버 변수 )
    private Node head;
    private Node tail;
    private int size = 0;

    public Node search(int index) {

        // 리스트의 범위를 벗어날 경우 예외 발생
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();

        /*
            단일 연결 리스트와 달리 prev 포인터를 가지고 있기 때문에 뒤에서 부터 찾을 수 있음
            때문에 반으로 나눠 검색함으로써 메소드의 효율을 높일 수 있다.
         */
        if (index < size / 2) {

            Node node = head;

            for (int i = 0; i < index; i++) node = node.next;

            return node;
        } else {

            Node node = tail;

            for (int i = size - 1; i > index; i--) node = node.prev;

            return node;
        }

    }

    public void addFirst(Object input) {

        // 입력받은 값을 데이터로 가지는 노드를 생성
        Node newNode = new Node(input);

        if (head == null) { // 리스트가 비어있는 경우, 새로운 노드가 리스트의 처음이자 마지막
            head = newNode;
            tail = newNode;
        } else { // 리스트가 있는 경우
            head.prev = newNode; // 기존 리스트의 head 의 prev 포인터가 새로운 노드를 가리키고
            newNode.next = head; // 새로운 노드의 next 포인터가 기존 리스트의 head 를 가리키도록 한 뒤
            head = newNode; // 리스트의 head 를 새로운 노드로 교체한다.
        }

        size++; // 리스트의 크기를 늘려준다.
    }

    public void addLast(Object input) {

        Node newNode = new Node(input);

        if (size == 0) { // 빈 리스트의 경우 기존 메소드 재활용
            addFirst(input);
        } else { // 빈 리스트가 아닌 경우
            tail.next = newNode; // tail 의 next 포인터가 새로운 노드를 지칭
            newNode.prev = tail; // 새로운 노드의 prev 포인터는 기존 리스트의 tail 을 지칭
            tail = newNode; // 리스트의 tail 을 새로운 노드로 교체
            size++;
        }
    }

    // 원하는 위치에 값을 넣을 수 있는 add 메소드
    public void add(int index, Object input) {

        if (index < 0 || index > size) throw new IndexOutOfBoundsException();

        // 리스트의 처음이나 끝에 값을 삽입하고자 할 경우, 기존 메소드 재활용
        if (index == 0) {
            addFirst(input);
        } else if (index == size) {
            addLast(input);
        } else {

            Node prevNode = search(index - 1); // 삽입하고자 하는 위치 이전의 노드
            Node nextNode = prevNode.next; // 삽입하고자 하는 위치의 기존 노드
            Node newNode = new Node(input); // 입력받은 값으로 만든 새로운 노드

            // 이전 노드, 새로운 노드, 기존 노드 순으로 뒤로 연결
            prevNode.next = newNode;
            newNode.next = nextNode;

            // 뒤에서부터 앞으로 다시 연결
            if (nextNode != null) nextNode.prev = newNode;
            newNode.prev = prevNode;

            size++;
        }
    }

    public Object removeFirst(){

        // temp 임시 변수를 만들어 기존 리스트의 head 노드를 담아준다.
        Node temp = head;
        // 리스트의 head 를 head 의 next 포인터가 가리키는 노드로 교체
        head = temp.next;

        // 삭제된 데이터를 리턴해주기 위해 temp 노드의 데이터를 변수에 담아 리턴
        Object removed = temp.data;

        // 임시 변수를 null 로 삭제
        temp = null;

        // 삭제했을 때, 빈 리스트가 아닐 경우 head 의 prev 포인터를 null 로 초기화
        if (head != null)
            head.prev = null;
        size--;

        return removed;
    }

    // 특정 위치의 데이터를 삭제하는 remove 메소드
    public Object remove(int index) {

        // 삭제하고자 하는 위치가 맨 처음일 경우 기존 메소드 활용
        if (index == 0) return removeFirst();


        Node prevNode = search(index - 1); // 삭제하고자 하는 위치 이전 노드
        Node deleteNode = prevNode.next; // 삭제하고자 하는 노드

        // 삭제하고자 하는 노드를 기존 리스트에서 분리
        prevNode.next = prevNode.next.next;
        if (prevNode.next != null) prevNode.next.prev = prevNode;

        Object removed = deleteNode.data;

        // 삭제하고자 하는 노드가 마지막 노드일 경우, tail 포인터를 이전 노드로 변경
        if (deleteNode == tail) tail = prevNode;

        deleteNode = null;
        size--;

        return removed;

    }

    public Object removeLast() {
        return remove(size - 1);
    }


    // 이하의 메소드들은 단일 연결 리스트와 큰 차이점이 없으니 설명은 생략
    public Object get(int index) {
        Node temp = search(index);
        return temp.data;
    }

    public void set(int index, Object value) {

        Node replaceNode = search(index);
        replaceNode.data = value;
    }

    public int indexOf(Object data) {

        Node temp = head;

        int index = 0;

        while (!temp.data.equals(data)) {
            temp = temp.next;
            index++;

            if (temp == null) return -1;
        }

        return index;
    }

    public boolean contains(Object input) {
        return indexOf(input) >= 0;
    }

    public void clear() {
        for (Node x = head; x != null;) {
            Node nextNode = x.next;
            x.data = null;
            x.next = null;
            x.prev = null;
            x = nextNode;
        }
        head = tail = null;
        size = 0;
    }

    public int size() {
        return size;
    }
}
