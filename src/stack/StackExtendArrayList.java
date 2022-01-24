package stack;

import list.ArrayList;

import java.util.EmptyStackException;

/*
    기존에 구현했던 ArrayList 클래스를 이용하면 Stack 을 쉽게 구현해볼 수 있다.

    하지만 ArrayList 를 상속받게 되면 자료 구조의 중간 데이터를 접근하여 조작할 수 있는데

    이는 Last In First Out 이라는 스택의 규칙에 어긋난다.

    따라서 스택을 제대로 사용하고 싶다면 Deque, ArrayDeque 인터페이스를 활용하는 것이 보다 바람직하다. 
 */

public class StackExtendArrayList < E > extends ArrayList< E > implements StackInterface < E > {

    public StackExtendArrayList() {
        super();
    }

    public StackExtendArrayList(int capacity) {
        super(capacity);
    }

    @Override
    public E push(E item) {
        addLast(item);
        return item;
    }

    @Override
    public E pop() {
        int length = size();

        E obj = remove(length - 1);

        return obj;
    }

    @Override
    public E peek() {
        int length = size();

        if (length == 0) throw new EmptyStackException();

        E obj = get(length - 1);

        return obj;
    }

    @Override
    public int search(Object value) {
        int index = indexOf(value);

        if (index >= 0) return size() - index;

        return -1;
    }

    @Override
    public boolean empty() {
        return size() == 0;
    }
}
